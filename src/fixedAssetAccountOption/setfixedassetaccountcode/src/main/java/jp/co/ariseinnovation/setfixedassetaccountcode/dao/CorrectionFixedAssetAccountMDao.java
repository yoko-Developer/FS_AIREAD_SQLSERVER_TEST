package jp.co.ariseinnovation.setfixedassetaccountcode.dao;

import jp.co.ariseinnovation.setfixedassetaccountcode.entity.CorrectionFixedAssetAccountMEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrectionFixedAssetAccountMDao extends JpaRepository<CorrectionFixedAssetAccountMEntity, String> {
}
