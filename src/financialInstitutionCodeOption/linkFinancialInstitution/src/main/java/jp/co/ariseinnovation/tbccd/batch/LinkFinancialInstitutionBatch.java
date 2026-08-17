package jp.co.ariseinnovation.tbccd.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import jp.co.ariseinnovation.tbccd.api.service.LinkFinancialInstitutionService;

/**
 * 金融機関コード紐付けバッチ処理
 */
@SpringBootApplication
@ComponentScan(basePackages = "jp.co.ariseinnovation.tbccd")
public class LinkFinancialInstitutionBatch implements CommandLineRunner {
    
    private static final Logger log = LoggerFactory.getLogger(LinkFinancialInstitutionBatch.class);
    
    @Autowired
    private LinkFinancialInstitutionService service;
    
    public static void main(String[] args) {
        SpringApplication.run(LinkFinancialInstitutionBatch.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Spring Bootの設定引数（--で始まる引数）をスキップ
        String csvPath = null;
        for (String arg : args) {
            if (!arg.startsWith("--")) {
                csvPath = arg;
                break;
            }
        }
        
        if (csvPath == null) {
            log.error("CSVファイルのパスを引数として指定してください");
            System.exit(1);
        }
        
        log.info("CSVファイル処理開始: {}", csvPath);
        
        File csvFile = new File(csvPath);
        if (!csvFile.exists()) {
            log.error("CSVファイルが見つかりません: {}", csvPath);
            System.exit(1);
        }
        
        try {
            processCsvFile(csvPath);
            log.info("CSVファイル処理完了: {}", csvPath);
        } catch (Exception e) {
            log.error("CSVファイル処理中にエラーが発生しました", e);
            throw e;
        }
    }
    
    /**
     * CSVファイルを処理する
     */
    private void processCsvFile(String csvPath) throws Exception {
        List<String[]> rows = new ArrayList<>();
        List<FinancialNameRow> financialNameRows = new ArrayList<>();
        String formId = null;
        
        // CSVファイルを読み込む
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(csvPath), StandardCharsets.UTF_8))) {
            
            String line;
            int rowIndex = 0;
            
            while ((line = reader.readLine()) != null) {
                String[] columns = parseCsvLine(line);
                rows.add(columns);
                
                // formidの値を取得
                if (columns.length > 4 && "formid".equals(columns[0])) {
                    formId = columns[4];
                    log.info("formid検出: {} (行{})", formId, rowIndex);
                }
                
                // 処理対象のformidかチェック
                if (formId != null && isTargetFormId(formId)) {
                    String itemName = columns.length > 0 ? columns[0] : "";
                    
                    // 金融機関名項目の候補をチェック
                    if (isFinancialNameItem(itemName) && isValidCombination(formId, itemName)) {
                        String financialName = columns.length > 4 ? columns[4] : "";
                        log.info("金融機関名行追加: {} (formId={}, itemName={}, 行{})", financialName, formId, itemName, rowIndex);
                        financialNameRows.add(new FinancialNameRow(rowIndex, columns, financialName, formId, itemName));
                    }
                }
                rowIndex++;
            }
        }
        
        log.info("金融機関名行数: {}", financialNameRows.size());
        
        // 各行に対して金融機関コードを検索してbank_code行を追加
        for (FinancialNameRow fnRow : financialNameRows) {
            String bankCode = service.getBankCode(fnRow.financialName);
            int conf = (bankCode != null && !bankCode.isEmpty()) ? 100 : 0;
            
            String bankCodeItemName = getBankCodeItemName(fnRow.formId, fnRow.itemName);
            String[] bankCodeRow = createBankCodeRow(fnRow.columns, bankCode, conf, bankCodeItemName);
            rows.add(bankCodeRow);
            
            log.info("金融機関名: {}, コード: {}, 信頼度: {}, 項目名: {}", fnRow.financialName, bankCode, conf, bankCodeItemName);
        }
        
        // CSVファイルを上書き保存
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(csvPath), StandardCharsets.UTF_8))) {
            
            for (String[] row : rows) {
                writer.write(formatCsvLine(row));
                writer.newLine();
            }
        }
    }
    
    /**
     * bank_code行を作成する
     */
    private String[] createBankCodeRow(String[] sourceRow, String bankCode, int conf, String bankCodeItemName) {
        String[] bankCodeRow = new String[sourceRow.length];
        
        // 基本的な値をコピー
        for (int i = 0; i < sourceRow.length; i++) {
            bankCodeRow[i] = sourceRow[i];
        }
        
        // ItemNameを設定（bank_code、paying_bank_code、discount_bank_codeなど）
        bankCodeRow[0] = bankCodeItemName;
        
        // Valueに金融機関コードをセット
        if (bankCodeRow.length > 4) {
            bankCodeRow[4] = bankCode != null ? bankCode : "";
        }
        
        // Confに信頼度をセット
        if (bankCodeRow.length > 5) {
            bankCodeRow[5] = String.valueOf(conf);
        }
        
        // KeyWordに「金融機関コード」をセット
        if (bankCodeRow.length > 6) {
            bankCodeRow[6] = "金融機関コード";
        }
        
        return bankCodeRow;
    }
    
    /**
     * 処理対象のformidかチェック
     */
    private boolean isTargetFormId(String formId) {
        return "1_depositsavings_0".equals(formId) ||
               "2_billsrecivable_0".equals(formId) ||
               "9_billspayable_0".equals(formId) ||
               "14_deptandinterestexpense_1".equals(formId);
    }
    
    /**
     * 金融機関名項目かどうかをformIdと組み合わせてチェック
     */
    private boolean isFinancialNameItem(String itemName) {
        return "financial_name".equals(itemName) ||
               "paying_bank_name".equals(itemName) ||
               "discount_bank_name".equals(itemName) ||
               "name".equals(itemName);
    }
    
    /**
     * formIdとitemNameの組み合わせが有効かチェック
     */
    private boolean isValidCombination(String formId, String itemName) {
        if ("1_depositsavings_0".equals(formId)) {
            return "financial_name".equals(itemName);
        } else if ("2_billsrecivable_0".equals(formId)) {
            return "paying_bank_name".equals(itemName) || "discount_bank_name".equals(itemName);
        } else if ("9_billspayable_0".equals(formId)) {
            return "paying_bank_name".equals(itemName);
        } else if ("14_deptandinterestexpense_1".equals(formId)) {
            return "name".equals(itemName);
        }
        return false;
    }
    
    /**
     * 出力するbank_code項目名を取得
     */
    private String getBankCodeItemName(String formId, String itemName) {
        if ("2_billsrecivable_0".equals(formId)) {
            if ("paying_bank_name".equals(itemName)) {
                return "paying_bank_code";
            } else if ("discount_bank_name".equals(itemName)) {
                return "discount_bank_code";
            }
        }
        return "bank_code";
    }
    
    /**
     * CSV行をパースする
     */
    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        
        return result.toArray(new String[0]);
    }
    
    /**
     * CSV行をフォーマットする
     */
    private String formatCsvLine(String[] columns) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            String value = columns[i];
            sb.append("\"");
            if (value != null) {
                sb.append(value.replace("\"", "\"\""));
            }
            sb.append("\"");
        }
        return sb.toString();
    }
    
    /**
     * 金融機関名行の情報
     */
    private static class FinancialNameRow {
        final int rowIndex;
        final String[] columns;
        final String financialName;
        final String formId;
        final String itemName;
        
        FinancialNameRow(int rowIndex, String[] columns, String financialName, String formId, String itemName) {
            this.rowIndex = rowIndex;
            this.columns = columns;
            this.financialName = financialName;
            this.formId = formId;
            this.itemName = itemName;
        }
    }
}