package com.devteam.module.company.service.hr.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.core.module.data.db.entity.StorageState;
import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.company.service.hr.entity.WorkContractTerm;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkContractTermRepository extends DataTPRepository<WorkContractTerm, Serializable> {

  @Modifying
  @Query("UPDATE WorkContractTerm wct SET wct.storageState = :storageState WHERE wct.id IN :ids")
  int setWorkContractTermsState(@Param("storageState") StorageState state, @Param("ids")  List<Long> ids);
  
  @Query("SELECT wct FROM WorkContractTerm wct WHERE wct.id IN :ids")
  public List<WorkContractTerm> findWorkContractTerms(@Param("ids") List<Long> ids);
}
