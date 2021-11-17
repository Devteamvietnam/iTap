package com.devteam.module.company.service.hr;

import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.DAOService;
import com.devteam.core.module.data.db.entity.ChangeStorageStateRequest;
import com.devteam.core.module.data.db.entity.StorageState;
import com.devteam.core.module.data.db.query.*;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.company.service.hr.entity.WorkAllowance;
import com.devteam.module.company.service.hr.repository.WorkAllowanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class WorkAllowanceLogic extends DAOService {
  static StorageState[] STATES       = StorageState.values();

  @Autowired
  private WorkAllowanceRepository workAllowanceRep;

  WorkAllowance getWorkAllowanceByCode(ClientInfo clientInfo, Company company, String code){
    return workAllowanceRep.getByCode(company.getId(), code);
  }

  WorkAllowance saveWorkAllowance(ClientInfo clientInfo, Company company, WorkAllowance workAllowance) {
    return workAllowanceRep.save(clientInfo, company, workAllowance);
  }

  List<WorkAllowance> searchWorkAllowances(ClientInfo client, Company company, SqlQueryParams params) {
    params.addParam("companyId", company.getId());
    SqlQuery query =
        new SqlQuery().
        ADD_TABLE(new EntityTable(WorkAllowance.class).selectAllFields()).
        FILTER( ClauseFilter.company(WorkAllowance.class)).
        FILTER(
             SearchFilter.isearch(WorkAllowance.class, new String[] {"code", "label"})).
        FILTER(
             OptionFilter.storageState(WorkAllowance.class),
             RangeFilter.modifiedTime(WorkAllowance.class)).
        ORDERBY(new String[] {"code", "modifiedTime"}, "modifiedTime", "DESC");
    return query(client, query, params, WorkAllowance.class); 
  }

  public boolean changeStorageState( ClientInfo client, Company company, ChangeStorageStateRequest req) {
    workAllowanceRep.setWorkAllowanceState(req.getNewStorageState(), req.getEntityIds());
    return true;
  }
}