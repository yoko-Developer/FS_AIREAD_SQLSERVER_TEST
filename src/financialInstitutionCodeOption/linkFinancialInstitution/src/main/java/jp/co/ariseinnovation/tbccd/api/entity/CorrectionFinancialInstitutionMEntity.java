package jp.co.ariseinnovation.tbccd.api.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 金融機関補足マスタ
 */
@Getter
@Setter
@Entity
@Table(name = "correction_financial_institution_m")
@ToString
public class CorrectionFinancialInstitutionMEntity implements Serializable {
   /** 金融機関補ID */
   @Id
   @Column(name = "correction_financial_institution_id")
   private Integer correctionFinancialInstitutionId;
   // /** 帳票種別ID */
   // @Column(name = "fs_form_type_id")
   // private String fsFormTypeId;
   // /** 検索方法 */
   // @Column(name = "search_method_id")
   // private Short searchMethodId;
   /** 補正前金融機関文字列 */
   @Column(name = "fluctuation_string")
   private String fluctuationString;
   // /** 補正前勘定科目文字列(検索用) */
   // @Column(name = "fluctuation_string_search")
   // private String fluctuationStringSearch;
   /** 補正後金融機関文字列 */
   @Column(name = "correction_string")
   private String correctionString;
}