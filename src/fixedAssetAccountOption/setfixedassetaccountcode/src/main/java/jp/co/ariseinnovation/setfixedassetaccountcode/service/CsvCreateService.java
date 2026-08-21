package jp.co.ariseinnovation.setfixedassetaccountcode.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jp.co.ariseinnovation.setfixedassetaccountcode.consts.CommonConsts;
import jp.co.ariseinnovation.setfixedassetaccountcode.dao.CorrectionDepreciationMethodMDao;
import jp.co.ariseinnovation.setfixedassetaccountcode.dao.CorrectionFixedAssetAccountMDao;
import jp.co.ariseinnovation.setfixedassetaccountcode.dao.DepreciationMDao;
import jp.co.ariseinnovation.setfixedassetaccountcode.dao.FixedAssetAccountMDao;
import jp.co.ariseinnovation.setfixedassetaccountcode.dao.TypeMDao;
import jp.co.ariseinnovation.setfixedassetaccountcode.entity.FixedAssetAccountMEntity;
import jp.co.ariseinnovation.setfixedassetaccountcode.entity.TypeMEntity;

@Service
public class CsvCreateService {

    private final static org.slf4j.Logger log = LoggerFactory.getLogger(CsvCreateService.class);

    private final static String FORM_TYPE_LOAN_PAYABLE = "02110";

    private Integer valueColIndex;

    @Autowired
    private TypeMDao typeMDao;

    @Autowired
    private CorrectionFixedAssetAccountMDao correctionFixedAssetAccountMDao;

    @Autowired
    private FixedAssetAccountMDao fixedAssetAccountMDao;

    // 償却方法補正マスタ
    @Autowired
    private CorrectionDepreciationMethodMDao correctionDepreciationMethodMDao;

    // 償却方法マスタ
    @Autowired
    private DepreciationMDao depreciationMDao;


    public List<List<String>> createRecord(
            Map<String, List<String>> groupedValues,
            Map<String, String> balanceValues,
            Map<String, String> depreciationMethodValues,
            Integer intdex,
            String bottomRowValue) throws IOException {

        this.valueColIndex = intdex;

        List<List<String>> fixedAssetAccountMRecords = new ArrayList<>();

        // 借入金
        List<TypeMEntity> TypeMList =
                typeMDao.searchByFormTypeId(FORM_TYPE_LOAN_PAYABLE);

        // 見出し行の科目コード
        String fixedAssetAccountCodeHead = null;

        // 前行の科目コード
        String fixedAssetAccountCodeBefore = bottomRowValue;

        // 一括更新対象indexリスト
        List<Integer> bulkUpdateIndexList = new ArrayList<>();

        int rowNo = 0;

        /*
         * 固定資産科目コードの紐付け処理
         */
        for (Map.Entry<String, List<String>> entry : groupedValues.entrySet()) {

            // key情報
            String key = entry.getKey();

            // key情報を分割
            List<String> keyList =
                    Arrays.asList(key.split("\\|"));

            // Value情報
            List<String> valueList = entry.getValue();

            // 合計行判定
            boolean isTotalRow = false;

            for (String value : valueList) {
                isTotalRow = checkTotalRow(value);

                if (isTotalRow) {
                    break;
                }
            }

            // Value補正
            valueList = correctValue(valueList);

            // Valueを結合
            String joinValue = String.join("", valueList);

            if (StringUtils.isEmpty(joinValue)) {
                fixedAssetAccountCodeBefore = null;
                continue;
            }

            // 結合Valueから取得される固定資産科目マスタ情報
            List<FixedAssetAccountMEntity> fixedAssetAccountMList =
                    fixedAssetAccountMDao
                            .searchByFixedAssetAccountName(joinValue);

            log.info("rowNo : " + rowNo);
            log.info("key : " + key);
            log.info("joinValue : " + joinValue);
            log.info("isTotalRow : " + isTotalRow);
            log.info("balanceValues.get(key)) : " + balanceValues.get(key));

            // 結合Valueが固定資産科目マスタの科目名を含むかを判定
            Optional<FixedAssetAccountMEntity> matchFixedAssetAccountMInfo =
                    getMatchFixedAssetAccountMInfo(
                            fixedAssetAccountMList,
                            joinValue);

            if (matchFixedAssetAccountMInfo.isPresent()) {

                // 科目名を含む
                if (StringUtils.isEmpty(balanceValues.get(key))) {

                    log.info("見出し科目");

                    // 金額を持たない場合、見出し科目
                    fixedAssetAccountCodeHead =
                            String.valueOf(
                                    matchFixedAssetAccountMInfo
                                            .get()
                                            .getFixedAssetAccountCode());

                    continue;

                } else {

                    // 金額あり
                    if (isTotalRow) {

                        // 合計行
                        if (bulkUpdateIndexList.isEmpty()) {

                            log.info("合計行・一括更新対象無し");

                            fixedAssetAccountCodeBefore =
                                    String.valueOf(
                                            matchFixedAssetAccountMInfo
                                                    .get()
                                                    .getFixedAssetAccountCode());

                        } else {

                            log.info("合計行・一括更新対象あり");

                            bulkUpdateValue(
                                    fixedAssetAccountMRecords,
                                    bulkUpdateIndexList,
                                    matchFixedAssetAccountMInfo);

                            bulkUpdateIndexList.clear();
                        }

                        // 見出し科目情報をクリア
                        fixedAssetAccountCodeHead = null;

                    } else {

                        log.info("科目名を含む内訳行");

                        fixedAssetAccountMRecords =
                                addRow(
                                        fixedAssetAccountMRecords,
                                        keyList,
                                        String.valueOf(
                                                matchFixedAssetAccountMInfo
                                                        .get()
                                                        .getFixedAssetAccountCode()),
                                        "100");

                        fixedAssetAccountCodeBefore =
                                String.valueOf(
                                        matchFixedAssetAccountMInfo
                                                .get()
                                                .getFixedAssetAccountCode());
                    }
                }

            } else {

                // 科目名を含まない場合
                if (StringUtils.isEmpty(fixedAssetAccountCodeHead)) {

                    // 見出し情報なし
                    if (StringUtils.isEmpty(fixedAssetAccountCodeBefore)) {

                        if (!isTotalRow) {

                            log.info(
                                    "科目名無し、見出し情報なし、前行情報なし、合計行でない");

                            bulkUpdateIndexList.add(rowNo);

                            fixedAssetAccountMRecords =
                                    addRow(
                                            fixedAssetAccountMRecords,
                                            keyList,
                                            "",
                                            "");
                        }

                    } else {

                        if (!isTotalRow) {

                            log.info(
                                    "科目名無し、見出し情報なし、前行情報あり");

                            fixedAssetAccountMRecords =
                                    addRow(
                                            fixedAssetAccountMRecords,
                                            keyList,
                                            fixedAssetAccountCodeBefore,
                                            "");
                        }
                    }

                } else {

                    log.info("科目名無し、見出し情報あり");

                    if (!isTotalRow) {

                        fixedAssetAccountMRecords =
                                addRow(
                                        fixedAssetAccountMRecords,
                                        keyList,
                                        fixedAssetAccountCodeHead,
                                        "");

                        fixedAssetAccountCodeBefore =
                                fixedAssetAccountCodeHead;

                    } else {

                        fixedAssetAccountCodeHead = null;
                    }
                }
            }

            rowNo++;
        }


        /*
         * 償却方法区分の紐付け処理
         *
         * FixLinkでDBへ設定していた処理を、
         * CSVへdepreciationCode行を追加する処理へ統合する。
         */
        var correctionDepreciationMethods =
                correctionDepreciationMethodMDao.findAll();

        for (Map.Entry<String, String> entry
                : depreciationMethodValues.entrySet()) {

            String depreciationMethod = entry.getValue();

            // 「方法」の値が空の場合は処理しない
            if (StringUtils.isBlank(depreciationMethod)) {
                continue;
            }

            log.info(
                    "depreciationMethod before correction : "
                            + depreciationMethod);

            // OCR値をそのまま初期値にする
            String depreciationName = depreciationMethod;

            // 償却方法補正マスタで補正
            var cdm =
                    correctionDepreciationMethods.stream()
                            .filter(i ->
                                    matchs(
                                            depreciationMethod,
                                            i.getBeforeCorrectionString()))
                            .findFirst();

            if (cdm.isPresent()) {
                depreciationName =
                        cdm.get().getAfterCorrectionString();
            }

            log.info(
                    "depreciationMethod after correction : "
                            + depreciationName);

            // 償却方法マスタからコードを取得
            var depreciation =
                    depreciationMDao
                            .findByDepreciationName(depreciationName);

            if (depreciation.isPresent()) {

                List<String> keyList =
                        Arrays.asList(
                                entry.getKey().split("\\|"));

                String depreciationCode =
                        String.valueOf(
                                depreciation
                                        .get()
                                        .getDepreciationCode());

                log.info(
                        "depreciationCode : "
                                + depreciationCode);

                // CSVへ償却方法区分コード行を追加
                fixedAssetAccountMRecords =
                        addDepreciationCodeRow(
                                fixedAssetAccountMRecords,
                                keyList,
                                depreciationCode,
                                "100");

            } else {

                log.info(
                        "償却方法マスタに該当なし : "
                                + depreciationName);
            }
        }

        return fixedAssetAccountMRecords;
    }


    // 合計行判定
    private boolean checkTotalRow(String joinValue) {

        boolean checkEqualVal =
                CommonConsts.equals(joinValue);

        boolean checkContainVal =
                CommonConsts.isContain(joinValue);

        boolean checkSuffixVal =
                joinValue.endsWith(CommonConsts.SUFFIX_CHECK);

        return checkEqualVal
                || checkContainVal
                || checkSuffixVal;
    }


    // 対象Valueが固定資産科目マスタの科目名を含んでいるかを判定
    private Optional<FixedAssetAccountMEntity>
    getMatchFixedAssetAccountMInfo(
            List<FixedAssetAccountMEntity> fixedAssetAccountMList,
            String checkValue) {

        return fixedAssetAccountMList.stream()
                .filter(fixedAssetAccountM ->
                        checkValue.contains(
                                fixedAssetAccountM
                                        .getFixedAssetAccountName()))
                .findFirst();
    }


    // 固定資産科目コード行追加
    private List<List<String>> addRow(
            List<List<String>> fixedAssetAccountMRecords,
            List<String> keyList,
            String fixedAssetAccountCode,
            String conf) {

        List<String> fixedAssetAccountMRecord =
                new ArrayList<>();

        fixedAssetAccountMRecord.add(
                "fixedAssetAccountCode");

        fixedAssetAccountMRecord.addAll(keyList);

        fixedAssetAccountMRecord.add(
                fixedAssetAccountCode);

        fixedAssetAccountMRecord.add(conf);
        fixedAssetAccountMRecord.add("");
        fixedAssetAccountMRecord.add("0");
        fixedAssetAccountMRecord.add("0");
        fixedAssetAccountMRecord.add("0");
        fixedAssetAccountMRecord.add("0");

        fixedAssetAccountMRecords.add(
                fixedAssetAccountMRecord);

        return fixedAssetAccountMRecords;
    }


    // 償却方法区分コード行追加
    private List<List<String>> addDepreciationCodeRow(
            List<List<String>> records,
            List<String> keyList,
            String depreciationCode,
            String conf) {

        List<String> record =
                new ArrayList<>();

        record.add("depreciationCode");
        record.addAll(keyList);
        record.add(depreciationCode);
        record.add(conf);
        record.add("");
        record.add("0");
        record.add("0");
        record.add("0");
        record.add("0");

        records.add(record);

        return records;
    }


    private List<List<String>> bulkUpdateValue(
            List<List<String>> fixedAssetAccountMRecords,
            List<Integer> bulkUpdateIndexList,
            Optional<FixedAssetAccountMEntity>
                    matchFixedAssetAccountMInfo) {

        for (Integer index : bulkUpdateIndexList) {

            fixedAssetAccountMRecords
                    .get(index)
                    .set(
                            valueColIndex,
                            String.valueOf(
                                    matchFixedAssetAccountMInfo
                                            .get()
                                            .getFixedAssetAccountCode()));
        }

        return fixedAssetAccountMRecords;
    }


    // 科目補正
    @Transactional
    public List<String> correctValue(
            List<String> valueList) {

        log.info("correct value start");

        var correctionFixedAssetAccounts =
                correctionFixedAssetAccountMDao.findAll();

        for (int i = 0;
             i < valueList.size();
             i++) {

            String value = valueList.get(i);

            var nm = value;

            var caa =
                    correctionFixedAssetAccounts.stream()
                            .filter(j ->
                                    matchs(
                                            value,
                                            j.getBeforeCorrectionString()))
                            .findFirst();

            if (caa.isPresent()) {
                nm =
                        caa.get()
                                .getAfterCorrectionString();
            }

            valueList.set(i, nm);
        }

        log.info("correct value end");

        return valueList;
    }


    private final static boolean matchs(
            String val,
            String regex) {

        if (Objects.isNull(val)) {
            return false;
        }

        return val.matches(regex);
    }
}
