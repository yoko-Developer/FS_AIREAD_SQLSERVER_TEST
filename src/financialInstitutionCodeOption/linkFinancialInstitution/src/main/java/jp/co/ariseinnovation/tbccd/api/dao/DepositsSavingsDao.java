package jp.co.ariseinnovation.tbccd.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.ariseinnovation.tbccd.api.entity.DepositsSavingsEntity;
import jp.co.ariseinnovation.tbccd.api.entity.OcrKey;

/**
 * 預貯金等の内訳書のDAO
 */
@Repository
public interface DepositsSavingsDao extends JpaRepository<DepositsSavingsEntity, OcrKey> {

    @Query("select o from DepositsSavingsEntity o where o.ocrResultId = :ocrResultId order by ocrResultId, pageNo, id")
    List<DepositsSavingsEntity> searchByOcrResultId(String ocrResultId);
}
