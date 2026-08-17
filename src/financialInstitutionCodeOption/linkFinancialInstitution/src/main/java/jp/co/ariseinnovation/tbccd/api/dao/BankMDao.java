package jp.co.ariseinnovation.tbccd.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.ariseinnovation.tbccd.api.entity.BankMEntity;

/**
 * 金融機関マスタのDAO
 */
@Repository
public interface BankMDao extends JpaRepository<BankMEntity, String> {
    @Query(value = "SELECT TOP 1 * FROM bank_m WHERE UPPER(REPLACE(bank_name, '銀行', '')) = :bankName ORDER BY branch_no DESC", nativeQuery = true)
    public BankMEntity searchBybankName(String bankName);

    @Query(value = "SELECT * FROM bank_m WHERE bank_name = :bankName", nativeQuery = true)
    public List<BankMEntity> searchAllBybankName(String bankName);

    @Query(value = "SELECT TOP 1 * FROM bank_m WHERE bank_code = :bankCode ORDER BY branch_no DESC", nativeQuery = true)
    public BankMEntity searchByBankCodeLatestBranch(String bankCode);
}