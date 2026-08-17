
package jp.co.ariseinnovation.setfixedassetaccountcode.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "correction_depreciation_method_m")
public class CorrectionDepreciationMethodMEntity {

    @Id
    @Column(name = "before_correction_string")
    private String beforeCorrectionString;

    @Column(name = "after_correction_string")
    private String afterCorrectionString;

    public String getBeforeCorrectionString() {
        return beforeCorrectionString;
    }

    public String getAfterCorrectionString() {
        return afterCorrectionString;
    }
}
