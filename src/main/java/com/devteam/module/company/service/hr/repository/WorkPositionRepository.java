package com.devteam.module.company.service.hr.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.core.module.data.db.entity.StorageState;
import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.company.service.hr.entity.WorkPosition;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface WorkPositionRepository extends DataTPRepository<WorkPosition, Serializable> {
  @Query( "SELECT w FROM WorkPosition w WHERE w.companyId = :companyId" )
  public List<WorkPosition> findAll(@Param("companyId") Long companyId);

  @Modifying
  @Query("UPDATE WorkPosition w SET w.storageState = :state WHERE w.code = :code")
  int setWorkPositionsState(@Param("state") StorageState state, @Param("code") String type);
  
  @Query("SELECT w FROM WorkPosition w WHERE w.id IN :ids")
  public List<WorkPosition> findWorkPositions(@Param("ids") List<Long> ids);
}
