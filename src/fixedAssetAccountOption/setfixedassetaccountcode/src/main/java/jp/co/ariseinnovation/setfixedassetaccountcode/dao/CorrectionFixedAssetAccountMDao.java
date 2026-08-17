package jp.co.ariseinnovation.setfixedassetaccountcode.dao;

import jp.co.ariseinnovation.setfixedassetaccountcode.entity.CorrectionFixedAssetAccountMEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorrectionFixedAssetAccountMDao extends JpaRepository<CorrectionFixedAssetAccountMEntity, String> {

    @Query(value = "select * from correction_fixed_asset_account_m where correction_fixed_asset_account_name LIKE CONCAT('%', :correctionFixedAssetAccountName, '%')", nativeQuery = true)
    List<CorrectionFixedAssetAccountMEntity> searchByCorrectionFixedAssetAccountName(@Param("correctionFixedAssetAccountName") String correctionFixedAssetAccountName);
}
