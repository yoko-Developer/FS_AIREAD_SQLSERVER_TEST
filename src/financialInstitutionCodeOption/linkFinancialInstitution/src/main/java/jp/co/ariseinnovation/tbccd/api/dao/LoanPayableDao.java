package jp.co.ariseinnovation.tbccd.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.ariseinnovation.tbccd.api.entity.LoanPayableEntity;
import jp.co.ariseinnovation.tbccd.api.entity.OcrKey;

/**
 * 借入金及び支払利子の内訳書のDAO
 */
@Repository
public interface LoanPayableDao extends JpaRepository<LoanPayableEntity, OcrKey> {

    @Query("select o from LoanPayableEntity o where o.ocrResultId = :ocrResultId and o.isTotalRow = 0 order by o.ocrResultId, o.pageNo, o.id")
    List<LoanPayableEntity> searchByOcrResultId(String ocrResultId);
}
