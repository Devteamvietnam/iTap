package com.devteam.module.company.service.hr.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.core.module.data.db.entity.StorageState;
import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.company.service.hr.entity.WorkTerm;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkTermRepository extends DataTPRepository<WorkTerm, Serializable> {

  @Modifying
  @Query("UPDATE WorkTerm wt SET wt.storageState = :storageState WHERE wt.id IN :ids")
  int setWorkTermsState(@Param("storageState") StorageState state, @Param("ids")  List<Long> ids);
  
  @Query("SELECT wt FROM WorkTerm wt WHERE wt.id IN :ids")
  public List<WorkTerm> findWorkTerms(@Param("ids") List<Long> ids);
}
