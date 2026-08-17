package jp.co.ariseinnovation.setfixedassetaccountcode.dao;

import jp.co.ariseinnovation.setfixedassetaccountcode.entity.CorrectionDepreciationMethodMEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorrectionDepreciationMethodMDao extends JpaRepository<CorrectionDepreciationMethodMEntity, Short> {

    @Query(value = "select * from correction_depreciation_method_m where :beforeString LIKE CONCAT('%', before_correction_string, '%')", nativeQuery = true)
    List<CorrectionDepreciationMethodMEntity> searchByBeforeCorrectionString(@Param("beforeString") String beforeString);
}
