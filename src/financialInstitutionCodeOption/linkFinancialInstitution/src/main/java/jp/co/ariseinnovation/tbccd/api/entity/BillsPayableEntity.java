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
 * 1.04 支払手形の内訳書
 */
@Getter
@Setter
@Entity
@Table(name = "bills_payable")
@IdClass(value = OcrKey.class)
@ToString
public class BillsPayableEntity extends OcrShare implements Serializable {
   /*
    * OCR結果ID
    */
   @Id
   @Column(name = "ocr_result_id")
   private String ocrResultId;
   /**
    * ページ番号
    */
   @Id
   @Column(name = "page_no")
   private int pageNo;
   /**
    * ID
    */
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

   /** 支払先 */
   @Column(name = "payee_name_original")
   private String payeeNameOriginal;

   /** 支払先(修正後) */
   @Column(name = "payee_name")
   private String payeeName;

   /** 支払先企業コード */
   @Column(name = "payee_com_code")
   private String payeeComCode;

   /** 支払先企業コードステータスID */
   @Column(name = "payee_com_code_status_id")
   private Short payeeComCodeStatusId;

   /** 支払先企業コード紐付けデータ種別 */
   @Column(name = "payee_comcd_relation_source_type_id")
   private Short payeeComcdRelationSourceTypeId;

   /** 支払先過去データ有フラグ */
   @Column(name = "payee_exist_comcd_relation_history_id")
   private Short payeeExistComcdRelationHistoryId;

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
   @Column(name = "paying_bank_name_code")
   private String payingBankNameCode;

   /** 支払銀行支店名 */
   @Column(name = "paying_bank_branch_name")
   private String payingBankBranchName;

   /** 摘要 */
   @Column(name = "description_original")
   private String descriptionOriginal;

   /** 摘要(修正後) */
   @Column(name = "description")
   private String description;

   /** 信頼値(登録・法人番号) */
   @Column(name = "conf_registration_number")
   private Short confRegistrationNumber;

   /** 信頼値(支払先) */
   @Column(name = "conf_payee_name")
   private Short confPayeeName;
      
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
   
   /** 座標X(支払先) */
   @Column(name = "coord_x_payee_name")
   private Short coordXPayeeName;
   
   /** 座標Y(支払先) */
   @Column(name = "coord_y_payee_name")
   private Short coordYPayeeName;
   
   /** 座標H(支払先) */
   @Column(name = "coord_h_payee_name")
   private Short coordHPayeeName;
   
   /** 座標W(支払先) */
   @Column(name = "coord_w_payee_name")
   private Short coordWPayeeName;
   
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
