package com.devteam.module.company.service.hr;

import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.DAOService;
import com.devteam.core.module.data.db.entity.ChangeStorageStateRequest;
import com.devteam.core.module.data.db.entity.StorageState;
import com.devteam.core.module.data.db.query.*;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.company.service.hr.entity.HRDepartment;
import com.devteam.module.company.service.hr.entity.WorkPosition;
import com.devteam.module.company.service.hr.repository.WorkPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class WorkPositionLogic extends DAOService {
  static StorageState[] STATES       = StorageState.values();
  
  @Autowired
  private WorkPositionRepository positionRepo;

  WorkPosition getWorkPositionByCode(ClientInfo client, Company company, String code) {
    return positionRepo.getByCode(client, company, code);
  }

  WorkPosition getWorkPosition(ClientInfo client, Company company, Long id) {
    return positionRepo.getById(id);
  }

  WorkPosition createWorkPosition(ClientInfo client, Company company, HRDepartment department, WorkPosition workPosition) {
    workPosition.withHRDepartment(department);
    return positionRepo.save(client, company, workPosition);
  }

  WorkPosition saveWorkPosition(ClientInfo client, Company company, WorkPosition workPosition) {
    return positionRepo.save(client, company, workPosition);
  }

  List<WorkPosition> searchWorkPositions(ClientInfo client, Company company, SqlQueryParams params) {
    params.addParam("companyId", company.getId());
    SqlQuery query =
        new SqlQuery()
        .ADD_TABLE(new EntityTable(WorkPosition.class).selectAllFields())
        .FILTER( ClauseFilter.company(WorkPosition.class))
        .FILTER(
             SearchFilter.isearch(WorkPosition.class, new String[] {"code", "label"}))
        .FILTER(
             OptionFilter.storageState(WorkPosition.class),
             RangeFilter.createdTime(WorkPosition.class), 
             RangeFilter.modifiedTime(WorkPosition.class))
        .ORDERBY(new String[] {"code", "modifiedTime"}, "modifiedTime", "DESC");
    if(params.hasParam("department")) {
      query
      .ADD_TABLE(new EntityTable(HRDepartment.class))
      .FILTER(new ClauseFilter(HRDepartment.class, "id", "=", WorkPosition.class, "hrDepartmentId"))
      .FILTER(new ClauseFilter(HRDepartment.class, "name", "=", ":department"));
    }
    return query(client, query, params, WorkPosition.class); 
  }

  public boolean changeStorageState( ClientInfo client, Company company, ChangeStorageStateRequest req) {
    List<WorkPosition> workPositions = positionRepo.findWorkPositions(req.getEntityIds());
    for(WorkPosition workPosition: workPositions) {
      positionRepo.setWorkPositionsState(req.getNewStorageState(), workPosition.getCode());
    }
    return true;
  }
}
