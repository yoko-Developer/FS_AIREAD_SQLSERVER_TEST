package jp.co.ariseinnovation.tbccd.api.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BreakdownDto {
    /** 金融機関名 */
    private String fiNameOriginal;
    /** 金融機関名(修正後) */
    private String fiName;
    /** 金融機関コード */
    private String fiCode;
    /** 支払銀行金融機関コード */
    private String payingBankCode;
    /** 割引銀行名及び支店名等 */
    private String discountBankNameOriginal;
    /** 割引銀行名及び支店名等(修正後) */
    private String discountBankName;
    /** 割引銀行金融機関コード */
    private String discountBankCode;
    /** 借入先名称(氏名) */
    private String lendersNameOriginal;
    /** 借入先名称(氏名)(修正後) */
    private String lendersName;
    /** 借入先コード */
    private String lendersBankCode;
    /** 支払銀行名称 */
    private String payingBankNameOriginal;
    /** 支払銀行名称(修正後) */
    private String payingBankName;
    /** 支払銀行金融機関コード */
    private String payingBankNameCode;
    /** 借入先所在地(住所)(修正後) */
    private String lendersLocation;
    /** 担保の内容(修正後) */
    private String collateral;
    /** 合計行フラグ */
    private Short isTotalRow;
    /** 支払銀行支店名(修正後) */
    private String payingBankBranchName;
    /** 支店名(修正後) */
    private String fiBranchName;

    public final static <T> List<BreakdownDto> from(List<T> res) {
        var ret = new ArrayList<BreakdownDto>();
        for (var i : res) {
            var breakdownDto = new BreakdownDto();
            BeanUtils.copyProperties(i, breakdownDto);
            ret.add(breakdownDto);
        }
        return ret;
    }

    public final static <T> void copyTo(List<BreakdownDto> source, List<T> target) {
        for (int i = 0; i < source.size(); i++) {
            BeanUtils.copyProperties(source.get(i), target.get(i));
        }
    }
}
