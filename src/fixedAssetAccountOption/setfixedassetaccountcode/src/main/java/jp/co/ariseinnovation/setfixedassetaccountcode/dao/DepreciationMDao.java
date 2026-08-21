package jp.co.ariseinnovation.setfixedassetaccountcode.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.ariseinnovation.setfixedassetaccountcode.entity.DepreciationMEntity;

@Repository
public interface DepreciationMDao extends JpaRepository<DepreciationMEntity, Integer> {

    Optional<DepreciationMEntity> findByDepreciationName(String depreciationName);
}
