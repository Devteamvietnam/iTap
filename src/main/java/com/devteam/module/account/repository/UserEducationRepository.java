package com.devteam.module.account.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.account.entity.UserEducation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserEducationRepository extends DataTPRepository<UserEducation, Serializable> {
  public List<UserEducation> findByLoginId(String loginId);
  
  @Modifying
  @Query("DELETE FROM UserEducation e WHERE e.loginId = :loginId AND e.id NOT IN (:validIdSet)")
  int deleteOrphan(@Param("loginId") String loginId, @Param("validIdSet") Set<Long> validIdSet);

  @Modifying
  @Query("DELETE FROM UserEducation e WHERE e.loginId = :loginId AND e.id IN (:ids)")
  int delete(@Param("loginId") String loginId, @Param("ids") List<Long> ids);

  public long deleteByLoginId(String loginId);
}
