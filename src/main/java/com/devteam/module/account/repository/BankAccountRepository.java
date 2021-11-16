package com.devteam.module.account.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.account.entity.BankAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BankAccountRepository extends DataTPRepository<BankAccount, Serializable> {
  public List<BankAccount> findByLoginId(String loginId);
  
  
  @Modifying
  @Query("DELETE FROM BankAccount b WHERE b.loginId = :loginId AND b.id NOT IN (:validIdSet)")
  int deleteOrphan(@Param("loginId") String loginId, @Param("validIdSet") Set<Long> validIdSet);
  
  public long deleteByLoginId(String loginId);

  @Modifying
  @Query("DELETE FROM BankAccount b WHERE b.loginId = :loginId AND b.id IN (:ids)")
  int delete(@Param("loginId") String loginId, @Param("ids") List<Long> ids);
}
