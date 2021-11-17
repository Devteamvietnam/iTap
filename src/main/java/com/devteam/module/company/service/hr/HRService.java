package com.devteam.module.company.service.hr;

import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.SqlMapRecord;
import com.devteam.core.module.data.db.activity.LogTransactionActivity;
import com.devteam.core.module.data.db.entity.ChangeStorageStateRequest;
import com.devteam.core.module.data.db.query.SqlQueryParams;
import com.devteam.module.account.entity.Account;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.company.core.security.CompanyAclModel;
import com.devteam.module.company.service.hr.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class HRService {
  @Autowired
  private DepartmentLogic departmentLogic;
  
  @Autowired
  private EmployeeLogic employeeLogic;
  
  @Autowired
  private WorkPositionLogic workPositionLogic;
  
  @Autowired
  private WorkAllowanceLogic workAllowanceLogic;

  @Autowired
  private EmployeeWorkContractLogic contractLogic;
  
  @Autowired 
  private WorkContractTermLogic workContractTermLogic;
  
  @Autowired 
  private WorkTermLogic workTermLogic;
  
  //HR Department
  @Transactional(readOnly = true)
  public HRDepartment getHRDepartment(ClientInfo clientInfo, Company company, Long id) {
    return departmentLogic.getHRDepartment(clientInfo, company, id);
  }
  
  @Transactional(readOnly = true)
  public HRDepartment getHRDepartment(ClientInfo clientInfo, Company company, String name) {
    return departmentLogic.getHRDepartment(clientInfo, company, name);
  }

  @Transactional(readOnly = true)
  public List<HRDepartment> findHRDepartmentChildren(ClientInfo client, Company company, Long groupId) {
    return departmentLogic.findHRDepartmentChildren(client, company, groupId);
  }
  
  @Transactional
  public HRDepartment createHRDepartment(ClientInfo clientInfo, Company company, HRDepartment parentDept, HRDepartment dept) {
    return departmentLogic.createHRDepartment(clientInfo, company, parentDept, dept);
  }
  
  
  @Transactional
  public HRDepartment saveHRDepartment(ClientInfo clientInfo, Company company, HRDepartment hRDepartment) {
    return departmentLogic.saveHRDepartment(clientInfo, company, hRDepartment);
  }
  
  @Transactional(readOnly = true)
  public List<HRDepartment> searchHRDepartment(ClientInfo client, Company company, SqlQueryParams params) {
    return departmentLogic.searchHRDepartment(client, company, params);
  }
  
  @Transactional
  public Boolean deleteHRDepartment(ClientInfo clienInfo, Company company, Long id) {
    return departmentLogic.delete(clienInfo, company, id);
  }
  
  @Transactional
  public boolean createHRDepartmentRelations(ClientInfo clientInfo, Company company, Long departmentId, List<Long> employeeIds) {
    return departmentLogic.createHRDepartmentRelations(clientInfo, company, departmentId, employeeIds);
  }
  
  @Transactional
  public boolean deleteHRDepartmentRelations(ClientInfo clientInfo, Company company, Long departmentId, List<Long> employeeIds) {
    return departmentLogic.deleteHRDepartmentRelations(clientInfo, company, departmentId, employeeIds);
  }

  //Employee
  @Transactional
  public Employee createEmployee(ClientInfo clientInfo, Company company, Account account, Employee employee) {
    return employeeLogic.createEmployee(clientInfo, company, account, employee);
  }

  @Transactional
  public Employee createEmployee(ClientInfo client, Company company, NewEmployeeModel model) {
    return employeeLogic.createEmployee(client, company, model);
  }
  
  @Transactional(readOnly = true)
  public EmployeeModel loadEmployeeModel(ClientInfo clientInfo, Company company, EmployeeModelRequest request) {
    return employeeLogic.loadEmployeeModel(clientInfo, company, request);
  }
  
  @Transactional(readOnly = true)
  public Employee getEmployee(ClientInfo clientInfo, Company company, String loginId) {
    return employeeLogic.getEmployee(clientInfo, company, loginId);
  }
  
  @Transactional(readOnly = true)
  public List<SqlMapRecord> searchEmployees(ClientInfo client, Company company, SqlQueryParams params) {
    return employeeLogic.searchEmployees(client, company, params);
  }
  
  @Transactional(readOnly = true)
  public List<Employee> findEmployees(ClientInfo client, Company company) { 
    return employeeLogic.findEmployees(company);
  }
  
  @Transactional(readOnly = true)
  public List<CompanyAclModel> findCompanyAcls(ClientInfo client, String loginId) {
    return employeeLogic.findCompanyAcls(client, loginId);
  }

  @Transactional
  public HRDepartmentEmployeeRelation createEmployeeDepartmentRelation(ClientInfo client, Company company, Employee empl, HRDepartment dept) {
    return employeeLogic.createEmployeeDepartmentRelation(client, company, empl, dept);
  }
  
  @Transactional
  public boolean changeEmployeesStorageState(ClientInfo client, Company company, ChangeStorageStateRequest req) {
    return employeeLogic.changeStorageState(client, company, req);
  }

  //Work Allowance
  @Transactional(readOnly=true)
  public WorkAllowance getWorkAllowanceByCode(ClientInfo clientInfo, Company company, String code) {
    return workAllowanceLogic.getWorkAllowanceByCode(clientInfo, company, code);
  }

  @Transactional
  public WorkAllowance saveWorkAllowance(ClientInfo clientInfo, Company company, WorkAllowance allowance) {
    return workAllowanceLogic.saveWorkAllowance(clientInfo, company, allowance);
  }

  @Transactional(readOnly=true)
  public List<WorkAllowance> searchWorkAllowances(ClientInfo clientInfo, Company company, SqlQueryParams params) {
    return workAllowanceLogic.searchWorkAllowances(clientInfo, company, params);
  }

  @Transactional
  public boolean changeWorkAllowancesStorageState(ClientInfo client, Company company, ChangeStorageStateRequest req) {
    return workAllowanceLogic.changeStorageState(client, company, req);
  }
  
  //Work Position
  @Transactional(readOnly = true)
  public WorkPosition getWorkPositionByCode(ClientInfo clientInfo, Company company, String code) {
    return workPositionLogic.getWorkPositionByCode(clientInfo, company, code);
  }
  
  @Transactional(readOnly = true)
  public WorkPosition getWorkPosition(ClientInfo clientInfo, Company company, Long id) {
    return workPositionLogic.getWorkPosition(clientInfo, company, id);
  }

  @Transactional
  public WorkPosition createWorkPosition(
      ClientInfo clientInfo, Company company, HRDepartment department, WorkPosition position) {
    return workPositionLogic.createWorkPosition(clientInfo, company, department, position);
  }

  @Transactional
  public WorkPosition saveWorkPosition(ClientInfo clientInfo, Company company, WorkPosition position) {
    return workPositionLogic.saveWorkPosition(clientInfo, company, position);
  }

  @Transactional(readOnly = true)
  public List<WorkPosition> searchWorkPositions(ClientInfo clientInfo, Company company, SqlQueryParams params) {
    return workPositionLogic.searchWorkPositions(clientInfo, company, params);
  }

  @Transactional
  public boolean changeWorkPositionsStorageState(ClientInfo client,Company company, ChangeStorageStateRequest req) {
    return workPositionLogic.changeStorageState(client, company, req);
  }

  //Employee Contract
  @Transactional
  public boolean changeEmployeeContractsStorageState(ClientInfo client, Company company, ChangeStorageStateRequest req) {
    return contractLogic.changeStorageState(client, company, req);
  }
  
  @Transactional(readOnly = true)
  public EmployeeWorkContract getEmployeeWorkContract(ClientInfo clientInfo, Company company, String code) {
    return contractLogic.getEmployeeWorkContract(clientInfo, company, code);
  }
  
  @Transactional(readOnly = true)
  public EmployeeWorkContract getEmployeeWorkContract(ClientInfo clientInfo, Company company, Long employeeContractId) {
    return contractLogic.getEmployeeWorkContract(clientInfo, employeeContractId);
  }

  @LogTransactionActivity(
      name=EmployeeWorkContract.TABLE_NAME,
      label = "Log change for Employee Work Contact")
  @Transactional
  public EmployeeWorkContract saveEmployeeWorkContract(ClientInfo clientInfo, Company company, EmployeeWorkContract position) {
    return contractLogic.saveEmployeeWorkContract(clientInfo, company, position);
  }
  
  @Transactional(readOnly = true)
  public List<SqlMapRecord> searchEmployeeWorkContract(ClientInfo client, Company company, SqlQueryParams params) {
    return contractLogic.searchEmployeeWorkContracts(client, company, params);
  }

  @Transactional(readOnly = true)
  public WorkPositionContract getWorkPositionContract(ClientInfo client, Company company, String code) {
    return contractLogic.getWorkPositionContract(client, company, code);
  }
  
  @Transactional
  public WorkPositionContract saveWorkPositionContract(ClientInfo client, Company company, WorkPositionContract workPositionContract) {
    return contractLogic.saveWorkPositionContract(client, company, workPositionContract);
  }
  
  @Transactional
  public boolean changeWorkPositionContractStorageState(ClientInfo client, Company company, ChangeStorageStateRequest req) {
    return contractLogic.changeWorkPositionContractState(client, company, req);
  }
  
  @Transactional(readOnly = true)
  public List<SqlMapRecord> searchWorkPositionContracts(ClientInfo client, Company company, SqlQueryParams params){
    return contractLogic.searchWorkPositionContracts(client, company, params);
  }
  
  @Transactional(readOnly = true)
  public List<WorkContractAttachment> findWorkContractAttachment(ClientInfo client, Company company, Long contractId) {
    return contractLogic.findWorkContractAttachments(client, company, contractId);
  }
  
  @Transactional
  public List<WorkContractAttachment> saveWorkContractAttachment(
      ClientInfo client, Company company, Long contractId, List<WorkContractAttachment> attachments) {
    return contractLogic.saveWorkContractAttachment(client, company, contractId, attachments);
  }
  
  @Transactional
  public WorkContractTerm saveWorkContractTerm(ClientInfo clientInfo, Company company, WorkContractTerm workContractTerm) {
    return workContractTermLogic.saveWorkContractTerm(clientInfo, company, workContractTerm);
  }
  

  @Transactional(readOnly = true)
  public List<WorkContractTerm> searchWorkContractTerms(ClientInfo client, Company company, SqlQueryParams params) {
    return workContractTermLogic.searchWorkContractTerms(client, company, params);
  }
  
  @Transactional
  public boolean changeWorkContractTermsStorageState(ClientInfo client, Company company, ChangeStorageStateRequest req) {
    return workContractTermLogic.changeStorageState(client, company, req);
  }
  
  @Transactional
  public WorkContractTerm getWorkContractTerm(ClientInfo client, Company company, Long id) {
    return workContractTermLogic.getById(client, company, id);
  }
  
  // WorkTerm
  @Transactional
  public WorkTerm getWorkTerm(ClientInfo client, Company company, Long id) {
    return workTermLogic.getById(client, company, id);
  }
  
  @Transactional
  public WorkTerm saveWorkTerm(ClientInfo clientInfo, Company company, WorkTerm workTerm) {
    return workTermLogic.saveWorkTerm(clientInfo, company, workTerm);
  }

  @Transactional(readOnly = true)
  public List<WorkTerm> searchWorkTerms(ClientInfo client, Company company, SqlQueryParams params) {
    return workTermLogic.searchWorkTerms(client, company, params);
  }
  
  @Transactional
  public boolean changeWorkTermsStorageState(ClientInfo client, Company company, ChangeStorageStateRequest req) {
    return workTermLogic.changeStorageState(client, company, req);
  }

}