package jp.co.ariseinnovation.tbccd.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.ariseinnovation.tbccd.api.entity.DepositsSavingsEntity;
import jp.co.ariseinnovation.tbccd.api.entity.NotesReceivableEntity;
import jp.co.ariseinnovation.tbccd.api.entity.OcrKey;

/**
 * 受取手形の内訳書のDAO
 */
@Repository
public interface NotesReceivableDao extends JpaRepository<NotesReceivableEntity, OcrKey> {
    @Query(value = "select o from NotesReceivableEntity o where o.ocrResultId = :ocrResultId order by ocrResultId, pageNo, id")
    public List<NotesReceivableEntity> searchByOcrResultId(String ocrResultId);
}
