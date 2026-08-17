package jp.co.ariseinnovation.tbccd.api.service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jp.co.ariseinnovation.tbccd.api.config.AppSetting;
import jp.co.ariseinnovation.tbccd.api.consts.BreakdownType;
import jp.co.ariseinnovation.tbccd.api.consts.StringConsts;
import jp.co.ariseinnovation.tbccd.api.dao.BankMDao;
import jp.co.ariseinnovation.tbccd.api.dao.BillsPayableDao;
import jp.co.ariseinnovation.tbccd.api.dao.CorrectionFinancialInstitutionMDao;
import jp.co.ariseinnovation.tbccd.api.dao.DepositsSavingsDao;
import jp.co.ariseinnovation.tbccd.api.dao.LoanPayableDao;
import jp.co.ariseinnovation.tbccd.api.dao.NotesReceivableDao;
import jp.co.ariseinnovation.tbccd.api.dto.BreakdownDto;
import jp.co.ariseinnovation.tbccd.api.entity.BankMEntity;
import jp.co.ariseinnovation.tbccd.api.entity.BillsPayableEntity;
import jp.co.ariseinnovation.tbccd.api.entity.CorrectionFinancialInstitutionMEntity;
import jp.co.ariseinnovation.tbccd.api.entity.DepositsSavingsEntity;
import jp.co.ariseinnovation.tbccd.api.entity.LoanPayableEntity;
import jp.co.ariseinnovation.tbccd.api.entity.NotesReceivableEntity;

/** 金融機関紐付けサービス */
@Service
public class LinkFinancialInstitutionService {
    private final static String BANK_PARNTER = "りそな";
    // private final static String BANK_EXCEPTION1 = "ブランク";
    private final static String BANK_EXCEPTION2 = "銀行";
    private final static String BANK_CODE_RISONA = "0010";
    private final static String BANK_CODE_SAITAMA_RISONA = "0017";
    // private final static String BANK_NAME_SAITAMA = "埼玉りそな銀行";
    private final static Logger log = LoggerFactory.getLogger(LinkFinancialInstitutionService.class);
    @Autowired
    private DepositsSavingsDao depositsSavingsDao;
    @Autowired
    private NotesReceivableDao notesReceivableDao;

    @Autowired
    private LoanPayableDao loanPayableDao;

    @Autowired
    private BillsPayableDao billsPayableDao;

    @Autowired
    private CorrectionFinancialInstitutionMDao correctionFinancialInstitutionMDao;

    @Autowired
    private BankMDao bankMDao;

    /*
     * private static final Map<String, String> LoanPayableNotBankNames
     * = Map.of(
     * "9985", "その他会社借入金",
     * "9998", "役員借入金");
     */

    // 借入金で合計行と判定する追加の条件
    private static final List<Pattern> TotalRowLoanPayableBankNames = List.of(
            Pattern.compile(".*年以内.*"), Pattern.compile(".*返済.*"), Pattern.compile(".*借入金.*"),
            Pattern.compile(".*短期借入.*"), Pattern.compile(".*長期借入.*"));

    /**
     * 特定文字列置換
     * 
     * @param name
     * @return
     */
    // public final static boolean isTotalRow(String name) {
    public final static boolean isTotalRow(BreakdownDto recordDto) {
        String name = recordDto.getLendersNameOriginal(); // 借入先名
        String location = recordDto.getLendersLocation(); // 借入先所在地
        String collateral = recordDto.getCollateral(); // 担保の内容

        if (StringUtils.isBlank(name)
                && StringUtils.isBlank(location)
                && StringUtils.isBlank(collateral)) {
            return false;
        }

        StringBuilder searchTargetBuilder = new StringBuilder((Objects.isNull(name)) ? "" : name)
                .append((Objects.isNull(location)) ? "" : location);
        String searchTarget = searchTargetBuilder.toString().trim();
        if (StringUtils.isBlank(searchTarget)) {
            // 借入先名 と 借入先所在地 がブランクの場合は担保の内容で判定する
            searchTarget = (Objects.isNull(collateral)) ? "" : collateral.trim();
        }

        if (StringUtils.isNotBlank(searchTarget)) {
            for (var pattern : TotalRowLoanPayableBankNames) {
                var match = pattern.matcher(searchTarget);
                if (match.find() && Objects.equals(match.group(), searchTarget)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void executeAll(String ocrResultId) {
        execute(ocrResultId, "DepositsSavings");
        execute(ocrResultId, "NotesReceivable");
        execute(ocrResultId, "LoanPayable");
        execute(ocrResultId, "BillsPayable");
    }

    /**
     * 金融機関名から金融機関コードを取得する（バッチ処理用）
     * 
     * @param financialName 金融機関名
     * @return 金融機関コード（見つからない場合はnull）
     */
    public String getBankCode(String financialName) {
        if (StringUtils.isBlank(financialName)) {
            return null;
        }

        // 金融機関名補正処理
        String correctedName = correction(BreakdownType.DepositsSavings, financialName);
        log.info("補正前: {}, 補正後: {}", financialName, correctedName);

        // 金融機関マスタから検索
        correctedName = correctedName.replaceAll("銀行.*", "");
        BankMEntity bankM = bankMDao.searchBybankName(correctedName);

        if (Objects.nonNull(bankM)) {
            return bankM.getBankCode();
        }
        
        return null;
    }

    /** 金融機関紐付け処理実行 */
    @Transactional()
    public void execute(String ocrResultId, String breakdownType) {

        log.info("execute : " + breakdownType);
        switch (breakdownType) {
            case "DepositsSavings":
                // 1.01 預貯金等の内訳書
                List<DepositsSavingsEntity> res;
                res = depositsSavingsDao.searchByOcrResultId(ocrResultId);

                if (!res.isEmpty()) {
                    var datas = BreakdownDto.from(res);
                    // 金融機関の紐付け処理
                    link(datas, BreakdownType.DepositsSavings);
                    BreakdownDto.copyTo(datas, res);
                    depositsSavingsDao.saveAll(res);
                }

                break;
            case "NotesReceivable":
                // 1.02 受取手形の内訳書
                List<NotesReceivableEntity> res1;
                res1 = notesReceivableDao.searchByOcrResultId(ocrResultId);

                if (!res1.isEmpty()) {
                    var datas = BreakdownDto.from(res1);
                    // 金融機関の紐付け処理
                    link(datas, BreakdownType.NotesReceivable);
                    // 金融機関更新処理
                    BreakdownDto.copyTo(datas, res1);
                    notesReceivableDao.saveAll(res1);
                }

                break;
            case "LoanPayable":
                // 1.06 借入金及び支払利子の内訳書
                List<LoanPayableEntity> res2;
                res2 = loanPayableDao.searchByOcrResultId(ocrResultId);

                if (!res2.isEmpty()) {
                    var datas = BreakdownDto.from(res2);
                    // 金融機関の紐付け処理
                    link(datas, BreakdownType.LoanPayable);
                    // 金融機関更新処理
                    BreakdownDto.copyTo(datas, res2);
                    loanPayableDao.saveAll(res2);
                }

                break;
            case "BillsPayable":
                // 1.04 支払手形の内訳書
                List<BillsPayableEntity> res3;
                res3 = billsPayableDao.searchByOcrResultId(ocrResultId);

                if (!res3.isEmpty()) {
                    var datas = BreakdownDto.from(res3);
                    // 金融機関の紐付け処理
                    link(datas, BreakdownType.BillsPayable);
                    // 金融機関更新処理
                    BreakdownDto.copyTo(datas, res3);
                    billsPayableDao.saveAll(res3);
                }

                break;
            default:
                break;
        }
    }

    // 3.02 金融機関(財務諸表)補正マスタ
    private String correction(BreakdownType breakdownType, String in) {
        log.info("=== correction() START ===");
        log.info("Input original: [{}]", in);
        
        if (Objects.isNull(in)) {
            log.info("Input is null, returning null");
            return in;
        }
        
        // 支店名等除去処理
        String val = StringConsts.replaceBankNameString(in);
        log.info("After replaceBankNameString: [{}]", val);
        
        // 全ての文字列を全角、大文字に変換する。
        val = StringConsts.halfToFull(val);
        log.info("After halfToFull: [{}]", val);
        
        val = StringConsts.toUpperCaseJpn(val.toUpperCase());
        log.info("After toUpperCaseJpn: [{}]", val);

        // 金融機関名補正処理
        log.info("Searching correction master with: [{}]", val);
        List<CorrectionFinancialInstitutionMEntity> ret;
        ret = correctionFinancialInstitutionMDao.searchByFluctuationString(val);
        log.info("Found {} correction master records", ret.size());
        
        if (ret.isEmpty()) {
            // 農協の支店名除去処理
            var noukyoNormalizedName = StringConsts.replaceNoukyoBranchString(val);
            log.info("After replaceNoukyoBranchString: [{}]", noukyoNormalizedName);
            
            if (!noukyoNormalizedName.equals(val)) {
                // 農協の支店名ありの場合
                log.info("Noukyou branch name found, searching again with: [{}]", noukyoNormalizedName);
                ret = correctionFinancialInstitutionMDao.searchByFluctuationString(noukyoNormalizedName);
                log.info("Found {} correction master records after noukyou search", ret.size());
                val = noukyoNormalizedName;
            }
            if (ret.isEmpty()) {
                log.info("No correction master match found, returning: [{}]", val);
                log.info("=== correction() END (no match) ===");
                return val;
            }
        }
        
        for (var temp : ret) {
            log.info("--- Checking correction pattern ---");
            log.info("Target value: [{}]", val);
            log.info("FluctuationString pattern (original): [{}]", temp.getFluctuationString());
            
            // 補正パターンの処理
            String patternString = temp.getFluctuationString();
            if (Objects.nonNull(patternString)) {
                // CSVインポート時のダブルクォーテーションを除去
                patternString = patternString.replaceAll("^\"|\"$", "");
                log.info("FluctuationString pattern (after removing quotes): [{}]", patternString);
                
                // 正規表現パターンなので、halfToFullは適用しない
                // ひらがなのみ大文字カタカナに変換（正規表現の特殊文字は変換しない）
                patternString = StringConsts.toUpperCaseJpn(patternString.toUpperCase());
                log.info("FluctuationString pattern (normalized): [{}]", patternString);
            }
            
            log.info("CorrectionString result: [{}]", temp.getCorrectionString());
            
            try {
                var regex = Pattern.compile(patternString);
                var match = regex.matcher(val);
                
                if (match.find()) {
                    log.info("Regex match.find() = true, matched group: [{}]", match.group());
                    log.info("Comparing: match.group()=[{}] vs val=[{}]", match.group(), val);
                    
                    if (Objects.equals(match.group(), val)) {
                        log.info("✓ Pattern matched completely!");
                        String correctionString = temp.getCorrectionString();
                        
                        // correction_stringからもダブルクォーテーションを除去
                        if (Objects.nonNull(correctionString)) {
                            correctionString = correctionString.replaceAll("^\"|\"$", "");
                        }
                        
                        // correction_stringは元の値をそのまま返す（カタカナ変換しない）
                        // bank_mテーブルはひらがなで登録されているため
                        log.info("Final correction result: [{}]", correctionString);
                        log.info("=== correction() END (matched) ===");
                        return correctionString;
                    } else {
                        log.info("✗ Pattern matched partially but not complete match");
                    }
                } else {
                    log.info("✗ Regex match.find() = false, no match");
                }
            } catch (Exception e) {
                log.warn("Failed to compile or match pattern: [{}], error: {}", patternString, e.getMessage());
            }
        }

        log.info("No complete pattern match found, returning: [{}]", val);
        log.info("=== correction() END (no complete match) ===");
        return val;
    }

    private void resetBank(String bankName, BankMEntity bankM) {
        if (Objects.nonNull(bankName)) {
            bankName = bankName.replaceAll("[　 ]", StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(bankName) || Objects.isNull(bankM)) {
            return;
        }

        // bankCode が "0010"(りそな銀行) の場合に埼玉りそな向けの特殊処理を行う
        String bankCode = Objects.requireNonNullElse(bankM.getBankCode(), "");
        if (!bankCode.equals(BANK_CODE_RISONA)) {
            return;
        }

        var bkNew = bankName;
        var idx = bankName.indexOf(BANK_PARNTER);
        if (idx > -1) {
            bkNew = bankName.substring(idx + 3);
        }
        // 支店名がない場合はりそな
        if (StringUtils.isBlank(bkNew) || BANK_EXCEPTION2.equals(bkNew)) {
            return;
        }
        bankM.setBankCode(BANK_CODE_SAITAMA_RISONA);
        //bankM.setBankName(BANK_NAME_SAITAMA);

    }

    // 金融機関の紐付け処理
    private void link(List<BreakdownDto> datas, BreakdownType breakdownType) {

        for (var data : datas) {

            String bankNameSearch = "";
            String bankNameSearch2 = ""; // 預貯金等の内訳書用

            // ③ 金融機関突合処理
            switch (breakdownType) {
                case DepositsSavings:
                    // 金融機関を取得(取得されなかった場合、支店名を取得)
                    bankNameSearch = StringUtils.isNotEmpty(data.getFiNameOriginal()) ? data.getFiNameOriginal() : data.getFiBranchName();
                    // ② 金融機関補正処理
                    log.info("before correction : " + bankNameSearch);
                    bankNameSearch = correction(breakdownType, bankNameSearch);
                    log.info("after correction : " + bankNameSearch);
                    break;
                case NotesReceivable:
                    bankNameSearch = data.getDiscountBankNameOriginal();
                    // ② 金融機関補正処理
                    log.info("before correction : " + bankNameSearch);
                    bankNameSearch = correction(breakdownType, bankNameSearch);
                    log.info("after correction : " + bankNameSearch);
                    // 金融機関を取得(取得されなかった場合、支店名を取得)
                    bankNameSearch2 = StringUtils.isNotEmpty(data.getPayingBankNameOriginal()) ? data.getDiscountBankNameOriginal() : data.getPayingBankBranchName();
                    // ② 金融機関補正処理
                    log.info("before correction2 : " + bankNameSearch2);
                    bankNameSearch2 = correction(breakdownType, bankNameSearch2);
                    log.info("after correction2 : " + bankNameSearch2);
                    break;
                case LoanPayable:
                    bankNameSearch = data.getLendersNameOriginal();
                    // ② 金融機関補正処理
                    log.info("before correction : " + bankNameSearch);

                    bankNameSearch = correction(breakdownType, bankNameSearch);
                    log.info("after correction : " + bankNameSearch);
                    break;
                case BillsPayable:
                    // 金融機関を取得(取得されなかった場合、支店名を取得)
                    bankNameSearch = StringUtils.isNotEmpty(data.getPayingBankNameOriginal()) ? data.getPayingBankNameOriginal() : data.getPayingBankBranchName();
                    // ② 金融機関補正処理
                    log.info("before correction : " + bankNameSearch);
                    bankNameSearch = correction(breakdownType, bankNameSearch);
                    log.info("after correction : " + bankNameSearch);
                    break;
                default:
                    break;
            }

            BankMEntity bankM = null;
            if (StringUtils.isNotBlank(bankNameSearch)) {
                log.info("=== Bank search START ===");
                log.info("Bank name before removing '銀行': [{}]", bankNameSearch);
                bankNameSearch = bankNameSearch.replaceAll("銀行.*", "");
                log.info("Bank name after removing '銀行': [{}]", bankNameSearch);
                
                bankM = bankMDao.searchBybankName(bankNameSearch);
                if (Objects.nonNull(bankM)) {
                    log.info("✓ Bank found: code=[{}], name=[{}], branch_no=[{}]", 
                        bankM.getBankCode(), bankM.getBankName(), bankM.getBranchNo());
                } else {
                    log.info("✗ Bank NOT found in bank_m table");
                }
                log.info("=== Bank search END ===");
            }

            // 預貯金等の内訳書用
            BankMEntity bankM2 = null;
            if (StringUtils.isNotBlank(bankNameSearch2)) {
                log.info("=== Bank search2 START ===");
                log.info("Bank name2 before removing '銀行': [{}]", bankNameSearch2);
                bankNameSearch2 = bankNameSearch2.replaceAll("銀行.*", "");
                log.info("Bank name2 after removing '銀行': [{}]", bankNameSearch2);
                
                bankM2 = bankMDao.searchBybankName(bankNameSearch2);
                if (Objects.nonNull(bankM2)) {
                    log.info("✓ Bank2 found: code=[{}], name=[{}], branch_no=[{}]", 
                        bankM2.getBankCode(), bankM2.getBankName(), bankM2.getBranchNo());
                } else {
                    log.info("✗ Bank2 NOT found in bank_m table");
                }
                log.info("=== Bank search2 END ===");
            }

            // 借入金向け特殊処理
            if (breakdownType == BreakdownType.LoanPayable) {
                if (Objects.isNull(bankM)) {
                    String lendersNameOriginal = data.getLendersNameOriginal();

                    // 合計行か判定する
                    if (isTotalRow(data)) {
                        // 合計行
                        data.setIsTotalRow((short) 1);
                    } else {
                        // 借入金の相手先が金融機関以外（金融機関コードが紐づかなかった）場合、
                        // その他会社借入金（9985） or 役員借入金（9998） を設定
                        bankM = new BankMEntity();
                        String normalizedBankCode = null;
                        if (StringConsts.isCorporate(lendersNameOriginal)) {
                            // 法人
                            normalizedBankCode = "9985";
                        } else {
                            // 法人以外
                            normalizedBankCode = "9998";
                        }

                        // 金融機関コードと紐づかなかった場合、相手先名を変更しない
                        // bankM.setBankName(LoanPayableNotBankNames.get(normalizedBankCode));
                        bankM.setBankName(lendersNameOriginal);
                        bankM.setBankCode(normalizedBankCode);
                    }
                }
            }

            // if (Objects.isNull(bankM)) {
            // 突合件数0件
            // data.setConfAccountTitle((short)1);
            // continue;
            // } else {
            if (AppSetting.current().isValid()) {
                var bankMNew = new BankMEntity();
                if (Objects.nonNull(bankM)) {
                    BeanUtils.copyProperties(bankM, bankMNew);
                    bankM = bankMNew;
                }
                var bankM2n = new BankMEntity();
                if (Objects.nonNull(bankM2)) {
                    BeanUtils.copyProperties(bankM2, bankM2n);
                    bankM2 = bankM2n;
                }
                switch (breakdownType) {
                    case DepositsSavings:
                        resetBank(Objects.requireNonNullElse(data.getFiNameOriginal(), "")
                                + Objects.requireNonNullElse(data.getFiBranchName(), ""), bankM);
                        break;
                    case NotesReceivable:
                        resetBank(data.getDiscountBankNameOriginal(), bankM);
                        resetBank(Objects.requireNonNullElse(data.getPayingBankNameOriginal(), "") +
                                Objects.requireNonNullElse(data.getPayingBankBranchName(), ""), bankM2);
                        break;
                    case LoanPayable:
                        resetBank(data.getLendersNameOriginal(), bankM);
                        break;
                    case BillsPayable:
                        resetBank(Objects.requireNonNullElse(data.getPayingBankNameOriginal(), "") +
                                Objects.requireNonNullElse(data.getPayingBankBranchName(), ""), bankM);
                        break;
                    default:
                        break;
                }

            }
            setBankData(data, breakdownType, bankM, bankM2);
            // }
        }
    }

    private void setBankData(BreakdownDto data, BreakdownType breakdownType, BankMEntity bankM, BankMEntity bankM2) {

        String bankName = (Objects.nonNull(bankM)) ? bankM.getBankName() : null;
        String bankCode = (Objects.nonNull(bankM)) ? bankM.getBankCode() : null;
        String bankName2 = (Objects.nonNull(bankM2)) ? bankM2.getBankName() : null;
        String bankCode2 = (Objects.nonNull(bankM2)) ? bankM2.getBankCode() : null;

        if (StringUtils.isNotBlank(bankCode)) {
            BankMEntity latestBranchBankM = bankMDao.searchByBankCodeLatestBranch(bankCode);
            if (Objects.nonNull(latestBranchBankM)) {
                bankName = latestBranchBankM.getBankName();
            }
        }
        if (StringUtils.isNotBlank(bankCode2)) {
            BankMEntity latestBranchBankM = bankMDao.searchByBankCodeLatestBranch(bankCode2);
            if (Objects.nonNull(latestBranchBankM)) {
                bankName2 = latestBranchBankM.getBankName();
            }
        }

        switch (breakdownType) {
            case DepositsSavings:
                data.setFiName(bankName);
                data.setFiCode(bankCode);
                break;
            case NotesReceivable:
                data.setDiscountBankName(bankName);
                data.setDiscountBankCode(bankCode);

                data.setPayingBankName(bankName2);
                data.setPayingBankCode(bankCode2);
                break;
            case LoanPayable:
                data.setLendersName(bankName);
                data.setLendersBankCode(bankCode);
                break;
            case BillsPayable:
                data.setPayingBankName(bankName);
                data.setPayingBankNameCode(bankCode);
                break;
            default:
                break;
        }
    }
}