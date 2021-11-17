package com.devteam.module.company.service.hr;

import java.util.ArrayList;
import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.DAOService;
import com.devteam.core.module.data.db.SqlMapRecord;
import com.devteam.core.module.data.db.entity.ChangeStorageStateRequest;
import com.devteam.core.module.data.db.query.*;
import com.devteam.core.util.ds.Collections;
import com.devteam.core.util.ds.Objects;
import com.devteam.core.util.error.ErrorType;
import com.devteam.core.util.error.RuntimeError;
import com.devteam.core.util.text.StringUtil;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.company.service.hr.entity.*;
import com.devteam.module.company.service.hr.repository.EmployeeWorkContractRepository;
import com.devteam.module.company.service.hr.repository.WorkContractAttachmentRepository;
import com.devteam.module.company.service.hr.repository.WorkPositionContractRepository;
import com.devteam.module.company.service.hr.entity.AbstractAllowance.AllowanceMethod;
import com.devteam.module.company.service.hr.entity.EmployeeBenefitContribution.BenefitContributionMethod;
import com.devteam.module.storage.CompanyStorage;
import com.devteam.module.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EmployeeWorkContractLogic extends DAOService {
  @Autowired
  private EmployeeWorkContractRepository contractRepo;

  @Autowired
  private WorkPositionContractRepository workPositionContractRepo;

  @Autowired
  private WorkContractAttachmentRepository contractAttachRepo;

  @Autowired
  private IStorageService storageService;

  public EmployeeWorkContract getEmployeeWorkContract(ClientInfo client, Long employeeContractId) {
    return contractRepo.getById(employeeContractId);
  }

  public EmployeeWorkContract getEmployeeWorkContract(ClientInfo client, Company company, String employeeContractCode) {
    return contractRepo.getByCode(company.getId(), employeeContractCode);
  }

  public EmployeeWorkContract saveEmployeeWorkContract(ClientInfo client, Company company, EmployeeWorkContract contract) {
    processWorkPositionContract(client, company, contract);
    contract.set(client, company);
    return contractRepo.save(contract);
  }
  
  private void processWorkPositionContract(ClientInfo client, Company company, EmployeeWorkContract contract) {
    WorkPositionContract workPositionContract = contract.getWorkPositionContract();
    Objects.assertNotNull(workPositionContract.getCode(), "workPositionContract should not be null");
    
    workPositionContract.set(client, company);
    workPositionContract.setEmployeeLoginId(contract.getEmployeeLoginId());
    workPositionContract.setEmployeeWorkContractCode(contract.getCode());
    processAllowance(workPositionContract);
    processBenefitContribution(workPositionContract);
    workPositionContract = workPositionContractRepo.save(workPositionContract);
    contract.setWorkPositionContract(workPositionContract);
  }

  public boolean changeStorageState(ClientInfo client, Company company, ChangeStorageStateRequest req) {
    contractRepo.setWorkContractsState(req.getNewStorageState(), req.getEntityIds());
    return true;
  }

  public List<EmployeeWorkContract> findActivesContracts(ClientInfo client, Company company,
      String employeeLoginId) {
    return contractRepo.findActivesByEmployee(company.getId(), employeeLoginId);
  }

  public List<SqlMapRecord> searchEmployeeWorkContracts(ClientInfo client, Company company, SqlQueryParams params) {
    params.addParam("companyId", company.getId());
    SqlQuery query = new SqlQuery().ADD_TABLE(new EntityTable(EmployeeWorkContract.class).selectAllFields())
        .JOIN(new Join("INNER JOIN", WorkPositionContract.class).addSelectField("salary", "salary")
            .addSelectField("bankAccountInfo", "bankAccountInfo").addSelectField("label", "workPositionContractLabel")
            .ON("id", EmployeeWorkContract.class, "work_position_contract_id"))
        .FILTER(ClauseFilter.company(EmployeeWorkContract.class))
        .FILTER(SearchFilter.isearch(EmployeeWorkContract.class, new String[] { "code" }))
        .FILTER(OptionFilter.storageState(EmployeeWorkContract.class),
            RangeFilter.createdTime(EmployeeWorkContract.class), RangeFilter.modifiedTime(EmployeeWorkContract.class))
        .ORDERBY(new String[] { "code", "modifiedTime" }, "modifiedTime", "ASC");
    if (params.hasParam("employeeLoginId")) {
      query.FILTER(new ClauseFilter(EmployeeWorkContract.class, "employeeLoginId", "=", ":employeeLoginId"));
    }
    query.mergeValue(params);
    return query(client, query, params).getSqlMapRecords();
  }

  // Work Position Contract
  public WorkPositionContract saveWorkPositionContract(ClientInfo client, Company company, WorkPositionContract workPositionContract) {
    String contractCode = workPositionContract.getEmployeeWorkContractCode();
    if (StringUtil.isEmpty(contractCode)) {
      throw new RuntimeError(ErrorType.IllegalArgument, "EmployeeWorkContractCode must not be empty");
    }

    EmployeeWorkContract contract = contractRepo.getByCode(company.getId(), contractCode);
    if (Objects.isNull(contract)) {
      throw new RuntimeError(ErrorType.EntityNotFound, "EmployeeWorkContract must be exist " + contractCode);
    }
    
    WorkPositionContract existWorkPositionContract = contract.getWorkPositionContract();
    existWorkPositionContract.setStatus(ContractStatus.CLOSED);
    workPositionContractRepo.save(existWorkPositionContract);

    workPositionContract.set(client, company);
    workPositionContract = workPositionContractRepo.save(workPositionContract);

    processAllowance(workPositionContract);
    processBenefitContribution(workPositionContract);
    contract.setWorkPositionContract(workPositionContract);
    contractRepo.save(contract);
    return workPositionContract;
  }
  
  private void processAllowance(WorkPositionContract pos) {
    List<EmployeeAllowance> allowances = new ArrayList<>();
    if(Collections.isNotEmpty(pos.getAllowances())) {
      for(EmployeeAllowance allowance : pos.getAllowances()) {
        if(allowance.getAllowanceMethod() == AllowanceMethod.FIXED_AMOUNT) {
          allowance.setSalaryPercentage(0);
        } else {
          if(allowance.getAllowanceMethod() == AllowanceMethod.WORKING_DAY) {
            allowance.setSalaryPercentage(0);
            allowance.setUnit("DAY");
          } else {
            allowance.setAmount(pos.getSocialInsuranceSalary() * allowance.getSalaryPercentage());
          }
        }
        allowances.add(allowance);
      }
      pos.setAllowances(allowances);
    }
  }
  
  private void processBenefitContribution(WorkPositionContract pos) {
    List<EmployeeBenefitContribution> contributions = new ArrayList<>();
    if(Collections.isNotEmpty(pos.getBenefitContributions())) {
      for(EmployeeBenefitContribution contribution : pos.getBenefitContributions()) {
        if(contribution.getContributionMethod() == BenefitContributionMethod.FIXED_AMOUNT) {
          contribution.setSalaryPercentage(0);
          contribution.setAmount(contribution.getQuantity() * contribution.getAmountPerUnit());
        } else {
          contribution.setAmountPerUnit(0);
          if(contribution.getContributionMethod() == BenefitContributionMethod.FULL_SALARY) {
            contribution.setAmount(contribution.getSalaryPercentage() * pos.getSalary());
          } else {
            contribution.setAmount(contribution.getSalaryPercentage() * pos.getSocialInsuranceSalary());
          } 
        }
        contributions.add(contribution);
      }
      pos.setBenefitContributions(contributions);
    }
  }

  public boolean changeWorkPositionContractState(ClientInfo client, Company company, ChangeStorageStateRequest req) {
    workPositionContractRepo.setWorkPositionContractState(req.getNewStorageState(), req.getEntityIds());
    return true;
  }

  public WorkPositionContract getWorkPositionContract(ClientInfo client, Company company, String code) {
    WorkPositionContract workPositionContract = workPositionContractRepo.getByCode(company.getId(), code);
    return workPositionContract;
  }

  public List<SqlMapRecord> searchWorkPositionContracts(ClientInfo client, Company company, SqlQueryParams params) {
    params.addParam("companyId", company.getId());
    SqlQuery query = new SqlQuery().ADD_TABLE(new EntityTable(WorkPositionContract.class).selectAllFields())
        .FILTER(ClauseFilter.company(WorkPositionContract.class))
        .FILTER(SearchFilter.isearch(WorkPositionContract.class, new String[] { "code", "label" }))
        .FILTER(OptionFilter.storageState(WorkPositionContract.class))
        .ORDERBY(new String[] { "code", "modifiedTime" }, "modifiedTime", "DESC");
    if (params.hasParam("employeeWorkContractCode")) {
      query.FILTER(
          new ClauseFilter(WorkPositionContract.class, "employeeWorkContractCode", "=", ":employeeWorkContractCode"));
    }
    if (params.hasParam("workPositionCode")) {
      query.FILTER(new ClauseFilter(WorkPositionContract.class, "workPositionCode", "=", ":workPositionCode"));
    }
    if (params.hasParam("employeeLoginId")) {
      query.FILTER(new ClauseFilter(WorkPositionContract.class, "employeeLoginId", "=", ":employeeLoginId"));
    }
    query.mergeValue(params);
    return query(client, query, params).getSqlMapRecords();
  }

  public List<EmployeeWorkContract> findActiveEmployeeWorkContracts(ClientInfo clientInfo, Company company) {
    return contractRepo.findActives(company.getId());
  }

  private String getTaskStoragePath(String employeeLoginId, String workContractCode) {
    return "apps/employee/" + employeeLoginId + "/" + workContractCode + "/attachments";
  }

  public List<WorkContractAttachment> findWorkContractAttachments(ClientInfo client, Company company, Long contractId) {
    return contractAttachRepo.findWorkContractAttachments(company.getId(), contractId);
  }

  public List<WorkContractAttachment> saveWorkContractAttachment(ClientInfo client, Company company, Long contractId,
      List<WorkContractAttachment> attachments) {
    EmployeeWorkContract contract = contractRepo.getById(contractId);
    String attachmentStoragePath = getTaskStoragePath(contract.getEmployeeLoginId(), contract.getCode());
    CompanyStorage storage = storageService.createCompanyStorage(client, company.getCode());
    storage.saveAttachments(attachmentStoragePath, attachments, true);
    for (WorkContractAttachment attachment : attachments) {
      attachment.setWorkContractId(contract.getId());
      
      attachment.set(client, company.getId());
    }
    attachments = contractAttachRepo.saveAll(attachments);
    List<Long> idSet = WorkContractAttachment.getIds(attachments);
    contractAttachRepo.deleteOrphan(company.getId(), contract.getId(), idSet);
    return attachments;
  }
}