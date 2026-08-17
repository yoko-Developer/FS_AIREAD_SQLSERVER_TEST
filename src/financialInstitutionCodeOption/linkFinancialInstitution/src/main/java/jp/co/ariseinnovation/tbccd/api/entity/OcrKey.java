package jp.co.ariseinnovation.tbccd.api.entity;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * キークラス
 */
@Setter
@Getter
@EqualsAndHashCode
public class OcrKey  implements Serializable {
   /*
    * OCR結果ID
    */
   private String ocrResultId;
   /**
    * ページ番号
    */
   private int pageNo;
   /**
    * ID
    */
   private int id;
}
