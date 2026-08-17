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
 * 借入金及び支払利子の内訳書
 */
@Getter
@Setter
@Entity
@Table(name = "loan_payable")
@IdClass(value = OcrKey.class)
@ToString
public class LoanPayableEntity extends OcrShare implements Serializable {
   /** OCR結果ID */
   @Id
   @Column(name = "ocr_result_id")
   private String ocrResultId;
   /** ページ番号 */
   @Id
   @Column(name = "page_no")
   private Integer pageNo;
   /** ID */
   @Id
   @Column(name = "id")
   private Integer id;

   /** 顧客店番 */
   @Column(name = "jgroupid_string")
   private String jgroupidString;

   /** 顧客番号 */
      @Column(name = "cif_number")
   private String cifNumber;

   /** 決算期 */
      @Column(name = "settlement_at")
   private String settlementAt;

   /** 借入先名称(氏名) */
      @Column(name = "lenders_name_original")
   private String lendersNameOriginal;

   /** 借入先名称(氏名)(修正後) */
      @Column(name = "lenders_name")
   private String lendersName;

   /** 借入先金融機関コード */
   @Column(name = "lenders_bank_code")
   private String lendersBankCode;

   /** 借入先所在地(住所) */
   @Column(name = "lenders_location_original")
   private String lendersLocationOriginal;

   /** 借入先所在地(住所)(修正後) */
   @Column(name = "lenders_location")
   private String lendersLocation;

   /** 法人・代表者との関係 */
   @Column(name = "relationship_original")
   private String relationshipOriginal;

   /** 法人・代表者との関係(修正後) */
   @Column(name = "relationship")
   private String relationship;

   /** 期末現在高 */
   @Column(name = "balance_original")
   private Long balanceOriginal;

   /** 期末現在高(修正後) */
   @Column(name = "balance")
   private Long balance;

   /** 期中の支払利子額 */
   @Column(name = "interest_original")
   private Long interestOriginal;

   /** 期中の支払利子額(修正後) */
   @Column(name = "interest")
   private Long interest;

   /** 利率 */
   @Column(name = "interest_rate_original")
   private String interestRateOriginal;

   /** 利率(修正後) */
   @Column(name = "interest_rate")
   private String interestRate;

   /** 担保の内容 */
   @Column(name = "collateral_original")
   private String collateralOriginal;

   /** 担保の内容(修正後) */
   @Column(name = "collateral")
   private String collateral;

   /** 種類コード */
   @Column(name = "loan_payable_type")
   private String loanPayableType;

   /** 信頼値(借入先名称(氏名)) */
   @Column(name = "conf_lenders_name")
   private Short confLendersName;

   /** 信頼値(借入先所在地(住所)) */
   @Column(name = "conf_lenders_location")
   private Short confLendersLocation;

   /** 信頼値(法人・代表者との関係) */
   @Column(name = "conf_relationship")
   private Short confRelationship;

   /** 信頼値(期末現在高) */
   @Column(name = "conf_balance")
   private Short confBalance;

   /** 信頼値(期中の支払利子額) */
   @Column(name = "conf_interest")
   private Short confInterest;

   /** 信頼値(利率) */
   @Column(name = "conf_interest_rate")
   private Short confInterestRate;

   /** 信頼値(担保の内容) */
   @Column(name = "conf_collateral")
   private Short confCollateral;

   /** 座標X(借入先名称(氏名)) */
   @Column(name = "coord_x_lenders_name")
   private Short coordXLendersName;

   /** 座標Y(借入先名称(氏名)) */
   @Column(name = "coord_y_lenders_name")
   private Short coordYLendersName;

   /** 座標H(借入先名称(氏名)) */
   @Column(name = "coord_h_lenders_name")
   private Short coordHLendersName;

   /** 座標W(借入先名称(氏名)) */
   @Column(name = "coord_w_lenders_name")
   private Short coordWLendersName;

   /** 座標X(借入先所在地(住所)) */
   @Column(name = "coord_x_lenders_location")
   private Short coordXLendersLocation;

   /** 座標Y(借入先所在地(住所)) */
   @Column(name = "coord_y_lenders_location")
   private Short coordYLendersLocation;

   /** 座標H(借入先所在地(住所)) */
   @Column(name = "coord_h_lenders_location")
   private Short coordHLendersLocation;

   /** 座標W(借入先所在地(住所)) */
   @Column(name = "coord_w_lenders_location")
   private Short coordWLendersLocation;

   /** 座標X(法人・代表者との関係) */
   @Column(name = "coord_x_relationship")
   private Short coordXRelationship;

   /** 座標Y(法人・代表者との関係) */
   @Column(name = "coord_y_relationship")
   private Short coordYRelationship;

   /** 座標H(法人・代表者との関係) */
   @Column(name = "coord_h_relationship")
   private Short coordHRelationship;

   /** 座標W(法人・代表者との関係) */
   @Column(name = "coord_w_relationship")
   private Short coordWRelationship;

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

   /** 座標X(期中の支払利子額) */
   @Column(name = "coord_x_interest")
   private Short coordXInterest;

   /** 座標Y(期中の支払利子額) */
   @Column(name = "coord_y_interest")
   private Short coordYInterest;

   /** 座標H(期中の支払利子額) */
   @Column(name = "coord_h_interest")
   private Short coordHInterest;

   /** 座標W(期中の支払利子額) */
   @Column(name = "coord_w_interest")
   private Short coordWInterest;

   /** 座標X(利率) */
   @Column(name = "coord_x_interest_rate")
   private Short coordXInterestRate;

   /** 座標Y(利率) */
   @Column(name = "coord_y_interest_rate")
   private Short coordYInterestRate;

   /** 座標H(利率) */
   @Column(name = "coord_h_interest_rate")
   private Short coordHInterestRate;

   /** 座標W(利率) */
   @Column(name = "coord_w_interest_rate")
   private Short coordWInterestRate;

   /** 座標X(担保の内容) */
   @Column(name = "coord_x_collateral")
   private Short coordXCollateral;

   /** 座標Y(担保の内容) */
   @Column(name = "coord_y_collateral")
   private Short coordYCollateral;

   /** 座標H(担保の内容) */
   @Column(name = "coord_h_collateral")
   private Short coordHCollateral;

   /** 座標W(担保の内容) */
   @Column(name = "coord_w_collateral")
   private Short coordWCollateral;

   /** 合計行フラグ */
   @Column(name = "is_total_row")
   private Short isTotalRow;

}