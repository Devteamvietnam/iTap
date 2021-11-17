package com.devteam.module.company.service.hr.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.core.module.data.db.entity.StorageState;
import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.company.service.hr.entity.WorkAllowance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface WorkAllowanceRepository extends DataTPRepository<WorkAllowance, Serializable> {
  @Query( "SELECT wa FROM WorkAllowance wa WHERE wa.companyId = :companyId AND wa.code = :code" )
  WorkAllowance getByCode(@Param("companyId") Long companyId, @Param("code") String code);

  @Modifying
  @Query("UPDATE WorkAllowance wa SET wa.storageState = :storageState WHERE wa.id IN :ids")
  int setWorkAllowanceState(@Param("storageState") StorageState state, @Param("ids")  List<Long> ids);

}
