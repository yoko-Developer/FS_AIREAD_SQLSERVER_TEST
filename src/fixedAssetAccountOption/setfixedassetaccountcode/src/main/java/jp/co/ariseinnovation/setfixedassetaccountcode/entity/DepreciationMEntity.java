package jp.co.ariseinnovation.setfixedassetaccountcode.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "depreciation_m")
public class DepreciationMEntity {

    @Id
    @Column(name = "depreciation_code")
    private Integer depreciationCode;

    @Column(name = "depreciation_name")
    private String depreciationName;

    public Integer getDepreciationCode() {
        return depreciationCode;
    }

    public String getDepreciationName() {
        return depreciationName;
    }
}
