/**
 * 
 */
package com.devteam.core.security.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.core.security.entity.AppPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppPermissonRepository extends JpaRepository<AppPermission, Serializable>{
  
  List<AppPermission> findByCompanyIdAndLoginId(String companyId, String loginId);
}
