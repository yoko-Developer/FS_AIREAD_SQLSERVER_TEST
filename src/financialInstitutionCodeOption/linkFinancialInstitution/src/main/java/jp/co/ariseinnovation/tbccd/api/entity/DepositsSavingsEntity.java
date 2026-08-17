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
 * 預貯金等の内訳書
 */
@Getter
@Setter
@Entity
@Table(name = "deposits_savings")
@IdClass(value = OcrKey.class)
@ToString
public class DepositsSavingsEntity extends OcrShare implements Serializable {
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

   /** 金融機関名 */
   @Column(name = "fi_name_original")
   private String fiNameOriginal;

   /** 金融機関名(修正後) */
   @Column(name = "fi_name")
   private String fiName;

   /** 金融機関コード */
   @Column(name = "fi_code")
   private String fiCode;

   /** 支店名 */
   @Column(name = "fi_branch_name_original")
   private String fiBranchNameOriginal;

   /** 支店名(修正後) */
   @Column(name = "fi_branch_name")
   private String fiBranchName;

   /** 口座番号 */
   @Column(name = "bank_account_number_original")
   private String bankAccountNumberOriginal;

   /** 口座番号(修正後) */
   @Column(name = "bank_account_number")
   private String bankAccountNumber;

   /** 期末現在高 */
   @Column(name = "balance_original")
   private Long balanceOriginal;

   /** 期末現在高(修正後) */
   @Column(name = "balance")
   private Long balance;

   /** 種類 */
   @Column(name = "deposits_savings_type_original")
   private String depositsSavingsTypeOriginal;
 
   /** 種類(修正後) */
   @Column(name = "deposits_savings_type")
   private String depositsSavingsType;

   /** 摘要 */
   @Column(name = "description_original")
   private String descriptionOriginal;

   /** 摘要(修正後) */
   @Column(name = "description")
   private String description;

   /** 信頼値(金融機関名) */
   @Column(name = "conf_fi_name")
   private Short confFiName;
   
   /** 信頼値(支店名) */
   @Column(name = "conf_fi_branch_name")
   private Short confFiBranchName;
    
   /** 信頼値(種類) */
   @Column(name = "conf_deposits_savings_type")
   private Short confDepositsSavingsType;
    
   /** 信頼値(口座番号) */
   @Column(name = "conf_bank_account_number")
   private Short confBankAccountNumber;
    
   /** 信頼値(期末現在高) */
   @Column(name = "conf_balance")
   private Short confBalance;

   /** 信頼値(摘要) */
   @Column(name = "conf_description")
   private Short confDescription;

   /** 座標X(金融機関名) */
   @Column(name = "coord_x_fi_name")
   private Short coordXFiName;

   /** 座標Y(金融機関名) */
   @Column(name = "coord_y_fi_name")
   private Short coordYFiName;

   /** 座標W(金融機関名) */
   @Column(name = "coord_w_fi_name")
   private Short coordWFiName;

   /** 座標H(金融機関名) */
   @Column(name = "coord_h_fi_name")
   private Short coordHFiName;

   /** 座標X(支店名) */
   @Column(name = "coord_x_fi_branch_name")
   private Short coordXFiBranchName;

   /** 座標Y(支店名) */
   @Column(name = "coord_y_fi_branch_name")
   private Short coordYFiBranchName;

   /** 座標H(支店名) */
   @Column(name = "coord_n_fi_branch_name")
   private Short coordNFiBranchName;

   /** 座標W(支店名) */
   @Column(name = "coord_w_fi_branch_name")
   private Short coordWFiBranchName;

   /** 座標X(種類) */
   @Column(name = "coord_x_deposits_savings_type")
   private Short coordXDepositsSavingsType;

   /** 座標Y(種類) */
   @Column(name = "coord_y_deposits_savings_type")
   private Short coordYDepositsSavingsType;

   /** 座標H(種類) */
   @Column(name = "coord_h_deposits_savings_type")
   private Short coordHDepositsSavingsType;

   /** 座標W(種類) */
   @Column(name = "coord_w_deposits_savings_type")
   private Short coordWDepositsSavingsType;

   /** 座標X(口座番号) */
   @Column(name = "coord_x_bank_account_number")
   private Short coordXBankAccountNumber;

   /** 座標Y(口座番号) */
   @Column(name = "coord_y_bank_account_number")
   private Short coordYBankAccountNumber;

   /** 座標H(口座番号) */
   @Column(name = "coord_h_bank_account_number")
   private Short coordHBankAccountNumber;

   /** 座標W(口座番号) */
   @Column(name = "coord_w_bank_account_number")
   private Short coordWBankAccountNumber;

   /** 座標X(期末現在高) */
   @Column(name = "coord_x_balance")
   private Short coordXBalance;

   /** 座標Y(期末現在高) */
   @Column(name = "coord_y_balance")
   private Short coordYBalance;

   /** 座標H(期末現在高) */
   @Column(name = "coord_h_balance")
   private Short coordHBalance;

   /** 座標W(期末現在高) */
   @Column(name = "coord_w_balance")
   private Short coordWBalance;

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