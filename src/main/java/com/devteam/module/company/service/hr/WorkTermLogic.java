package com.devteam.module.company.service.hr;

import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.DAOService;
import com.devteam.core.module.data.db.entity.ChangeStorageStateRequest;
import com.devteam.core.module.data.db.query.*;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.company.service.hr.entity.WorkTerm;
import com.devteam.module.company.service.hr.repository.WorkTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkTermLogic extends DAOService {

  @Autowired
  private WorkTermRepository workTermRepo;

  public WorkTerm getById(ClientInfo clientInfo, Company company, Long id) {
    return workTermRepo.getById(id);
  }

  public WorkTerm saveWorkTerm(ClientInfo clientInfo, Company company, WorkTerm workTerm) {
    workTerm.set(clientInfo, company);
    return workTermRepo.save(clientInfo, company, workTerm);
  }

  public boolean changeStorageState(ClientInfo client, Company company, ChangeStorageStateRequest req) {
    workTermRepo.setWorkTermsState(req.getNewStorageState(), req.getEntityIds());
    return true;
  }

  List<WorkTerm> searchWorkTerms(ClientInfo client, Company company, SqlQueryParams params) {
    params.addParam("companyId", company.getId());
    SqlQuery query = new SqlQuery().ADD_TABLE(new EntityTable(WorkTerm.class).selectAllFields())
        .FILTER( ClauseFilter.company(WorkTerm.class))
        .FILTER( SearchFilter.isearch(WorkTerm.class, new String[] { "category", "label", "term" }))
        .FILTER( OptionFilter.storageState(WorkTerm.class),
             RangeFilter.createdTime(WorkTerm.class),
             RangeFilter.modifiedTime(WorkTerm.class))
        .ORDERBY(new String[] { "modifiedTime" }, "modifiedTime", "DESC");
    return query(client, query, params, WorkTerm.class);
  }

}
