package com.devteam.module.account.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.account.entity.UserIdentity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserIdentityRepository extends DataTPRepository<UserIdentity, Serializable> {
  public List<UserIdentity> findByLoginId(String loginId);
  
  @Modifying
  @Query("DELETE FROM UserIdentity i WHERE i.loginId = :loginId AND i.id NOT IN (:validIdSet)")
  int deleteOrphan(@Param("loginId") String loginId, @Param("validIdSet") Set<Long> validIdSet);
  

  @Modifying
  @Query("DELETE FROM UserIdentity i WHERE i.loginId = :loginId AND i.id IN (:ids)")
  int delete(@Param("loginId") String loginId, @Param("ids") List<Long> ids);

  public long deleteByLoginId(String loginId);

}
