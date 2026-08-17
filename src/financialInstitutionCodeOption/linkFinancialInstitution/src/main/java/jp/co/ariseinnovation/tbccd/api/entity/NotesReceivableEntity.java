package jp.co.ariseinnovation.tbccd.api.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 受取手形の内訳書
 */
@Getter
@Setter
@Entity
@Table(name = "notes_receivable")
@IdClass(value = OcrKey.class)
@ToString
public class NotesReceivableEntity extends OcrShare implements Serializable {
   /** OCR結果ID */
   @Id
   @Column(name = "ocr_result_id")
   private String ocrResultId;

   /** ページ番号 */
   @Id
   @Column(name = "page_no")
   private int pageNo;

   /** ID */
   @Id
   @Column(name = "id")
   private int id;

   /** 顧客店番 */
    @Column(name = "jgroupid_string")
   private String jgroupidString;

   /** 顧客番号 */
    @Column(name = "cif_number")
   private String cifNumber;

   /** 決算期 */
    @Column(name = "settlement_at")
   private String settlementAt;

   /** 登録・法人番号 */
    @Column(name = "registration_number_original")
   private String registrationNumberOriginal;

   /** 登録・法人番号(修正後) */
    @Column(name = "registration_number")
   private String registrationNumber;

   /** 振出人 */
    @Column(name = "maker_name_original")
   private String makerNameOriginal;

   /** 振出人(修正後) */
    @Column(name = "maker_name")
   private String makerName;

   /** 振出人企業コード */
    @Column(name = "maker_com_code")
   private String makerComCode;

   /** 振出人企業コードステータスID */
    @Column(name = "maker_com_code_status_id")
   private Short makerComCodeStatusId;

   /** 振出人企業コード紐付けデータ種別 */
    @Column(name = "maker_comcd_relation_source_type_id")
   private Short makerComcdRelationSourceTypeId;

   /** 振出人過去データ有フラグ */
    @Column(name = "maker_exist_comcd_relation_history_id")
   private Short makerExistComcdRelationHistoryId;

   /** 振出年月日 */
    @Column(name = "issue_date_original")
   private String issueDateOriginal;

   /** 振出年月日(修正後) */
    @Column(name = "issue_date")
   private String issueDate;

   /** 支払期日 */
    @Column(name = "due_date_original")
   private String dueDateOriginal;

   /** 支払期日(修正後) */
    @Column(name = "due_date")
   private String dueDate;

   /** 金額 */
   @Column(name = "balance_original")
   private Long balanceOriginal;

   /** 金額(修正後) */
   @Column(name = "balance")
   private Long balance;

   /** 支払銀行名称 */
   @Column(name = "paying_bank_name_original")
   private String payingBankNameOriginal;

   /** 支払銀行名称(修正後) */
   @Column(name = "paying_bank_name")
   private String payingBankName;

   /** 支払銀行金融機関コード */
   @Column(name = "paying_bank_code")
   private String payingBankCode;

   /** 支払銀行支店名 */
   @Column(name = "paying_bank_branch_name_original")
   private String payingBankBranchNameOriginal;

   /** 支払銀行支店名(修正後) */
   @Column(name = "paying_bank_branch_name")
   private String payingBankBranchName;

   /** 割引銀行名及び支店名等 */
   @Column(name = "discount_bank_name_original")
   private String discountBankNameOriginal;

   /** 割引銀行名及び支店名等(修正後)  */
   @Column(name = "discount_bank_name")
   private String discountBankName;

   /** 割引銀行金融機関コード */
   @Column(name = "discount_bank_code")
   private String discountBankCode;

   /** 摘要 */
   @Column(name = "description_original")
   private String descriptionOriginal;

   /** 摘要 */
   @Column(name = "description")
   private String description;

   /** 信頼値(振出人) */
   @Column(name = "conf_maker_name")
   private Short confMakerName;

   /** 信頼値(登録番号) */
   @Column(name = "conf_registration_number")
   private Short confRegistrationNumber;

   /** 信頼値(法人番号) */
   // @Column(name = "conf_corporate_number")
   // private Short confCorporateNumber;

   /** 信頼値(振出年月日) */
   @Column(name = "conf_issue_date")
   private Short confIssueDate;

   /** 信頼値(支払期日) */
   @Column(name = "conf_due_date")
   private Short confDueDate;

   /** 信頼値(金額) */
   @Column(name = "conf_balance")
   private Short confBalance;

   /** 信頼値(支払銀行名称) */
   @Column(name = "conf_paying_bank_name")
   private Short confPayingBankName;

   /** 信頼値(支払銀行支店名) */
   @Column(name = "conf_paying_bank_branch_name")
   private Short confPayingBankBranchName;

   /** 信頼値(割引銀行名及び支店名等) */
   @Column(name = "conf_discount_bank_name")
   private Short confDiscountBankName;

   /** 信頼値(摘要) */
   @Column(name = "conf_description")
   private Short confDescription;

   /** 座標X(登録・法人番号) */
   @Column(name = "coord_x_registration_number")
   private Short coordXRegistrationNumber;

   /** 座標Y(登録・法人番号) */
   @Column(name = "coord_y_registration_number")
   private Short coordYRegistrationNumber;

   /** 座標H(登録・法人番号) */
   @Column(name = "coord_h_registration_number")
   private Short coordHRegistrationNumber;

   /** 座標W(登録・法人番号) */
   @Column(name = "coord_w_registration_number")
   private Short coordWRegistrationNumber;

   /** 座標X(振出人) */
   @Column(name = "coord_x_maker_name")
   private Short coordXMakerName;

   /** 座標Y(振出人) */
   @Column(name = "coord_y_maker_name")
   private Short coordYMakerName;

   /** 座標H(振出人) */
   @Column(name = "coord_h_maker_name")
   private Short coordHMakerName;

   /** 座標W(振出人) */
   @Column(name = "coord_w_maker_name")
   private Short coordWMakerName;

   /** 座標X(振出年月日) */
   @Column(name = "coord_x_issue_date")
   private Short coordXIssueDate;

   /** 座標Y(振出年月日) */
   @Column(name = "coord_y_issue_date")
   private Short coordYIssueDate;

   /** 座標H(振出年月日) */
   @Column(name = "coord_h_issue_date")
   private Short coordHIssueDate;

   /** 座標W(振出年月日) */
   @Column(name = "coord_w_issue_date")
   private Short coordWIssueDate;

   /** 座標X(支払期日) */
   @Column(name = "coord_x_due_date")
   private Short coordXDueDate;

   /** 座標Y(支払期日) */
   @Column(name = "coord_y_due_date")
   private Short coordYDueDate;

   /** 座標H(支払期日) */
   @Column(name = "coord_h_due_date")
   private Short coordHDueDate;

   /** 座標W(支払期日) */
   @Column(name = "coord_w_due_date")
   private Short coordWDueDate;

   /** 座標X(金額) */
   @Column(name = "coord_x_balance")
   private Short coordXBalance;

   /** 座標Y(金額) */
   @Column(name = "coord_y_balance")
   private Short coordYBalance;

   /** 座標H(金額) */
   @Column(name = "coord_h_balance")
   private Short coordHBalance;

   /** 座標W(金額) */
   @Column(name = "coord_w_balance")
   private Short coordWBalance;

   /** 座標X(支払銀行名称) */
   @Column(name = "coord_x_paying_bank_name")
   private Short coordXPayingBankName;

   /** 座標Y(支払銀行名称) */
   @Column(name = "coord_y_paying_bank_name")
   private Short coordYPayingBankName;

   /** 座標H(支払銀行名称) */
   @Column(name = "coord_h_paying_bank_name")
   private Short coordHPayingBankName;

   /** 座標W(支払銀行名称) */
   @Column(name = "coord_w_paying_bank_name")
   private Short coordWPayingBankName;

   /** 座標X(支払銀行支店名) */
   @Column(name = "coord_x_paying_bank_branch_name")
   private Short coordXPayingBankBranchName;

   /** 座標Y(支払銀行支店名) */
   @Column(name = "coord_y_paying_bank_branch_name")
   private Short coordYPayingBankBranchName;

   /** 座標H(支払銀行支店名) */
   @Column(name = "coord_h_paying_bank_branch_name")
   private Short coordHPayingBankBranchName;

   /** 座標W(支払銀行支店名) */
   @Column(name = "coord_w_paying_bank_branch_name")
   private Short coordWPayingBankBranchName;

   /** 座標X(割引銀行名及び支店名等) */
   @Column(name = "coord_x_discount_bank_name")
   private Short coordXDiscountBankName;

   /** 座標Y(割引銀行名及び支店名等) */
   @Column(name = "coord_y_discount_bank_name")
   private Short coordYDiscountBankName;

   /** 座標H(割引銀行名及び支店名等) */
   @Column(name = "coord_h_discount_bank_name")
   private Short coordHDiscountBankName;

   /** 座標W(割引銀行名及び支店名等) */
   @Column(name = "coord_w_discount_bank_name")
   private Short coordWDiscountBankName;

   /** 座標X(摘要) */
   @Column(name = "coord_x_description")
   private Short coordXDescription;

   /** 座標Y(摘要) */
   @Column(name = "coord_y_description")
   private Short coordYDescription;

   /** 座標H(摘要) */
   @Column(name = "coord_h_description")
   private Short coordHDescription;

   /** 座標W(摘要) */
   @Column(name = "coord_w_description")
   private Short coordWDescription;
}