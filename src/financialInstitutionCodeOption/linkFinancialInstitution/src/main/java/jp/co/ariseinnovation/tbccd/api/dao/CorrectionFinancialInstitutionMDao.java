package jp.co.ariseinnovation.tbccd.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.ariseinnovation.tbccd.api.entity.CorrectionFinancialInstitutionMEntity;

/**
 * 金融機関補足マスタのDAO
 */
@Repository
public interface CorrectionFinancialInstitutionMDao extends JpaRepository<CorrectionFinancialInstitutionMEntity, Integer> {

    // PostgreSQL版: WHERE :fluctuationString ~ o.fluctuation_string (正規表現マッチング)
    // SQL Serverには正規表現マッチング演算子がないため、全件取得してアプリケーション側でマッチングを行う
    @Query(value = "SELECT o.* FROM correction_financial_institution_m o", nativeQuery = true)
    List<CorrectionFinancialInstitutionMEntity> searchByFluctuationString(String fluctuationString);
}
