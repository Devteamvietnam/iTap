package com.devteam.module.company.service.hr.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.core.module.data.db.entity.StorageState;
import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.company.service.hr.entity.EmployeeWorkContract;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EmployeeWorkContractRepository extends DataTPRepository<EmployeeWorkContract, Serializable> {
  
  @Query( "SELECT wc FROM EmployeeWorkContract wc WHERE wc.companyId = :companyId AND wc.status = 'ACTIVE'" )
  public List<EmployeeWorkContract> findActives(@Param("companyId") Long companyId);

  @Query( "SELECT wc FROM EmployeeWorkContract wc WHERE wc.companyId = :companyId AND wc.employeeLoginId = :employeeLoginId AND wc.status = 'ACTIVE'")
  public List<EmployeeWorkContract> findActivesByEmployee(@Param("companyId") Long companyId, @Param("employeeLoginId") String employeeLoginId);
  
  @Query( "SELECT wc FROM EmployeeWorkContract wc WHERE wc.companyId = :companyId AND wc.code = :code" )
  public EmployeeWorkContract getByCode(@Param("companyId") Long companyId, @Param("code") String code);
  
  @Modifying
  @Query("UPDATE EmployeeWorkContract wc SET wc.storageState = :storageState WHERE wc.id IN :ids")
  int setWorkContractsState(@Param("storageState") StorageState state, @Param("ids")  List<Long> ids);
  
  @Query("SELECT wc FROM EmployeeWorkContract wc WHERE wc.id IN :ids")
  public List<EmployeeWorkContract> findWorkContracts(@Param("ids") List<Long> ids);
}
