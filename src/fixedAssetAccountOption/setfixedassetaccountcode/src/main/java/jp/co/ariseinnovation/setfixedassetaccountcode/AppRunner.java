package jp.co.ariseinnovation.setfixedassetaccountcode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import jp.co.ariseinnovation.setfixedassetaccountcode.service.CsvCreateService;

@Component
public class AppRunner implements ApplicationRunner {

    private final static org.slf4j.Logger log = LoggerFactory.getLogger(ApplicationRunner.class);

    @Autowired
    private CsvCreateService csvCreateService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        var runArgs = args.getNonOptionArgs();

        if (runArgs.isEmpty() || runArgs.size() != 1) {
            throw new Exception();
        }

        var inputFilePath = runArgs.get(0) + "\\csv_AIRead";

        exportCsv(inputFilePath);
    }

    private void exportCsv(String inputFilePath) throws FileNotFoundException, IOException {

        // 入力ディレクトリ
        File csvDir = new File(inputFilePath);

        // .csvでファイルを抽出
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".csv");
            }
        };

        // CSVファイルの一覧を取得
        File[] csvList = csvDir.listFiles(filter);

        if (csvList == null) {
            log.error("CSVファイルを取得できません: " + inputFilePath);
            return;
        }

        String bottomRowValue = null;

        for (File inputFile : csvList) {

            // 固定資産台帳判定
            try (Stream<String> lines = Files.lines(inputFile.toPath())) {

                if (lines.noneMatch(line -> line.contains("05_010_00"))) {
                    continue;
                }

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

            // 入力CSV
            Reader reader = new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8);

            // CSVファイルの読み込み
            @SuppressWarnings("deprecation")
            CSVParser csvParser = CSVFormat.DEFAULT
                            .withFirstRecordAsHeader()
                            .parse(reader);

            // "Value"の列番号を取得
            Integer valueColIndex = csvParser.getHeaderMap().get("Value");

            if (Objects.isNull(valueColIndex)) {
                log.info("ヘッダ項目の形式が不正のため、処理を終了します");
                reader.close();
                return;
            }

            // Valueの結合情報
            Map<String, List<String>> groupedValues = new LinkedHashMap<>();

            // 金額情報
            Map<String, String> balanceValues = new LinkedHashMap<>();

            // 償却方法情報
            Map<String, String> depreciationMethodValues = new LinkedHashMap<>();

            boolean alreadyProcessed = false;

            for (CSVRecord record : csvParser) {

                // すでに処理済みの場合は、このCSVファイルをスキップ
                if (StringUtils.equals(
                        record.get("ItemName"),
                        "fixedAssetAccountCode")) {

                    log.info(
                            "fixedAssetAccountCode行追加済みのため、このCSVファイルをスキップします。");

                    alreadyProcessed = true;
                    break;
                }

                String grupId = record.get("GrupID");

                if (StringUtils.equals(grupId, "-1")) {
                    continue;
                }

                String page = record.get("Page");
                String gId = record.get("GID");

                String checkKeys =
                        page + "|" + gId + "|" + grupId;

                // 償却方法情報をまとめる
                if (StringUtils.equals(
                        record.get("ItemName"),
                        "方法")) {

                    depreciationMethodValues.put(
                            checkKeys,
                            record.get("Value"));
                }

                if (groupedValues
                        .computeIfAbsent(
                                checkKeys,
                                k -> new ArrayList<>())
                        .size() <= 6) {

                    groupedValues
                            .computeIfAbsent(
                                    checkKeys,
                                    k -> new ArrayList<>())
                            .add(record.get("Value"));
                }

                // 金額情報をまとめる
                if (StringUtils.equals(
                        record.get("ItemName"),
                        "取得価額")) {

                    balanceValues.put(
                            checkKeys,
                            record.get("Value"));
                }
            }

            if (alreadyProcessed) {
                reader.close();
                continue;
            }

            reader.close();

            // 科目コード＋償却方法区分コード生成
            List<List<String>> fixedAssetAccountCodeRecords =
                    csvCreateService.createRecord(
                            groupedValues,
                            balanceValues,
                            depreciationMethodValues,
                            valueColIndex,
                            bottomRowValue);

            /*
             * 次ページへ引き継ぐ科目コードを取得。
             * depreciationCode行ではなく、
             * fixedAssetAccountCode行の最後のValueだけを保持する。
             */
            for (int i = fixedAssetAccountCodeRecords.size() - 1;
                 i >= 0;
                 i--) {

                List<String> record =
                        fixedAssetAccountCodeRecords.get(i);

                if (!record.isEmpty()
                        && StringUtils.equals(
                        record.get(0),
                        "fixedAssetAccountCode")) {

                    bottomRowValue =
                            record.get(valueColIndex);

                    break;
                }
            }

            // CSVへ追記
            var bw =
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(
                                            inputFile,
                                            true),
                                    StandardCharsets.UTF_8));

            try {

                for (List<String> outputRecord
                        : fixedAssetAccountCodeRecords) {

                    for (int j = 0;
                         j < outputRecord.size();
                         j++) {

                        bw.write(
                                "\""
                                        + outputRecord.get(j)
                                        + "\"");

                        if (j != outputRecord.size() - 1) {
                            bw.write(",");
                        }
                    }

                    bw.newLine();
                }

            } catch (Exception e) {

                log.error(e.getMessage(), e);

            } finally {

                bw.close();
            }
        }
    }
}
