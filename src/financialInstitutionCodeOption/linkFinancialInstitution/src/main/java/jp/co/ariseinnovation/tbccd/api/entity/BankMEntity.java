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
 * 金融機関マスタ
 */
@Getter
@Setter
@Entity
@Table(name = "bank_m")
@ToString
public class BankMEntity extends OcrShare implements Serializable, Cloneable {
   /** 金融機関コード */
   @Id
   @Column(name = "bank_code")
   private String bankCode;
   /** 金融機関番号 */
   @Column(name = "branch_no")
   private Short branchNo;
   /** 金融機関名 */
   @Column(name = "bank_name")
   private String bankName;
   /** 表示用金融機関名 */
   @Column(name = "bank_name_view")
   private String bankNameView;
}