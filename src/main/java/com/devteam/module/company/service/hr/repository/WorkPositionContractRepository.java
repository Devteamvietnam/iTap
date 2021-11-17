package com.devteam.module.company.service.hr.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.core.module.data.db.entity.StorageState;
import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.company.service.hr.entity.WorkPositionContract;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface WorkPositionContractRepository extends DataTPRepository<WorkPositionContract, Serializable> {
  @Query( "SELECT a FROM WorkPositionContract a WHERE a.companyId = :companyId AND a.code = :code" )
  public WorkPositionContract getByCode(@Param("companyId") Long companyId, @Param("code") String code);

  @Modifying
  @Query("UPDATE WorkPositionContract ca SET ca.storageState = :storageState WHERE ca.id IN :ids")
  int setWorkPositionContractState(@Param("storageState") StorageState state, @Param("ids")  List<Long> ids);
  
}
