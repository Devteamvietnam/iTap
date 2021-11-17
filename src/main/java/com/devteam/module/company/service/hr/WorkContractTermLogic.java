package com.devteam.module.company.service.hr;

import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.DAOService;
import com.devteam.core.module.data.db.entity.ChangeStorageStateRequest;
import com.devteam.core.module.data.db.entity.StorageState;
import com.devteam.core.module.data.db.query.*;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.company.service.hr.entity.WorkContractTerm;
import com.devteam.module.company.service.hr.repository.WorkContractTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkContractTermLogic extends DAOService {

  @Autowired
  private WorkContractTermRepository workContractTermRepo;

  public WorkContractTerm getById(ClientInfo clientInfo, Company company, Long id) {
    return workContractTermRepo.getById(id);
  }

  public WorkContractTerm saveWorkContractTerm(ClientInfo clientInfo, Company company, WorkContractTerm workContractTerm) {
    return workContractTermRepo.save(clientInfo, company, workContractTerm);
  }

  public boolean changeStorageState(ClientInfo client, Company company, ChangeStorageStateRequest req) {
    workContractTermRepo.setWorkContractTermsState(req.getNewStorageState(), req.getEntityIds());
    return true;
  }

  List<WorkContractTerm> searchWorkContractTerms(ClientInfo client, Company company, SqlQueryParams params) {
    params.addParam("companyId", company.getId());
    SqlQuery query = new SqlQuery().ADD_TABLE(new EntityTable(WorkContractTerm.class).selectAllFields())
        .FILTER(new ClauseFilter(WorkContractTerm.class, "companyId", "=", ":companyId"))
        .FILTER(new SearchFilter(WorkContractTerm.class, new String[] { "category", "label", "term" }, "LIKE", "search"))
        .FILTER(new OptionFilter(WorkContractTerm.class, "storageState", "=", StorageState.ALL, StorageState.ACTIVE),
            new RangeFilter(WorkContractTerm.class, "createdTime"),
            new RangeFilter(WorkContractTerm.class, "modifiedTime"))
        .ORDERBY(new String[] { "modifiedTime" }, "modifiedTime", "DESC");
    return query(client, query, params, WorkContractTerm.class);
  }

}
