package jp.co.ariseinnovation.setfixedassetaccountcode.dao;

import jp.co.ariseinnovation.setfixedassetaccountcode.entity.CorrectionDepreciationMethodMEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrectionDepreciationMethodMDao
    extends JpaRepository<CorrectionDepreciationMethodMEntity, Short> {

}
