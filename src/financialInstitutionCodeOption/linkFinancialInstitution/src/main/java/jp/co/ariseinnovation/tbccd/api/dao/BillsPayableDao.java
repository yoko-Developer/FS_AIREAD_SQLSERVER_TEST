package jp.co.ariseinnovation.tbccd.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.ariseinnovation.tbccd.api.entity.BillsPayableEntity;
import jp.co.ariseinnovation.tbccd.api.entity.DepositsSavingsEntity;
import jp.co.ariseinnovation.tbccd.api.entity.OcrKey;

/**
 * 支払手形の内訳書のDAO
 */
@Repository
public interface BillsPayableDao extends JpaRepository<BillsPayableEntity, OcrKey> {
    @Query("select o from BillsPayableEntity o where o.ocrResultId = :ocrResultId order by ocrResultId, pageNo, id")
    public List<BillsPayableEntity> searchByOcrResultId(String ocrResultId);
}
