package com.devteam.module.company.service.hr.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.query.SqlQueryParams;
import com.devteam.core.module.http.upload.UploadResource;
import com.devteam.core.module.http.upload.UploadService;
import com.devteam.module.account.AccountService;
import com.devteam.module.company.core.data.db.sample.CompanyEntityAssert;
import com.devteam.module.company.core.data.db.sample.CompanySampleData;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.company.service.hr.HRService;
import com.devteam.module.company.service.hr.entity.*;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class DBModuleDataAssert extends CompanySampleData {

  @Autowired
  HRService hrService;

  @Autowired
  AccountService accountService;
  
  @Autowired
  private UploadService uploadService ;
  

  // Company HR Department
  public class CompanyHrDepartmentAssert extends CompanyEntityAssert<HRDepartment> {
    public CompanyHrDepartmentAssert(ClientInfo client, Company company, HRDepartment department) {
      super(client, company, department);
      this.methods = new EntityServiceMethods() {
        public HRDepartment load() {
          return hrService.getHRDepartment(client, company, department.getName());
        }

        public HRDepartment save(HRDepartment entity) {
          return hrService.saveHRDepartment(client, company, entity);
        }
      };
    }
  }

  //Company Employee
  public class CompanyEmployeeAssert extends CompanyEntityAssert<Employee> {
    public CompanyEmployeeAssert(ClientInfo client, Company company, Employee employee, String departmentName) {
      super(client, company, employee);
      this.methods = new EntityServiceMethods() {
        public Employee load() {
          return hrService.getEmployee(client, company, entity.getLoginId());
        }
        
        public List<?> search(SqlQueryParams params) {
            params.addParam("department", departmentName);
            return hrService.searchEmployees(client, company, params);
          }

        public boolean archive() {
          return hrService.changeEmployeesStorageState(client, company, createArchivedStorageRequest(entity));
        }
      };
    }
  }

  //Company Employee WorkContract
  public class CompanyEmployeeWorkContractAssert extends CompanyEntityAssert<EmployeeWorkContract> {
    public CompanyEmployeeWorkContractAssert(ClientInfo client, Company company, EmployeeWorkContract contract) {
      super(client, company, contract);
      this.methods = new EntityServiceMethods() {
        public EmployeeWorkContract load() {
          return hrService.getEmployeeWorkContract(client, company, contract.getCode());
        }

        public EmployeeWorkContract save(EmployeeWorkContract entity) {
         return  hrService.saveEmployeeWorkContract(client, company, entity);
        }

        public List<?> search(SqlQueryParams params) {
          params.addParam("loginId", contract.getEmployeeLoginId());
          return hrService.searchEmployeeWorkContract(client, company, params);
        }

        public boolean archive() {
          return hrService.changeEmployeeContractsStorageState(client, company, createArchivedStorageRequest(entity));
        }
      };
    }
    
    public CompanyEmployeeWorkContractAssert assertAddAttachment(String ... file) {
      List<WorkContractAttachment> attachments = new ArrayList<>();
      for(String selFile : file) {
        File uploadFile = new File(selFile);
        UploadResource uploadResource = uploadService.save("/upload", "/upload", uploadFile);
        WorkContractAttachment attachment = new WorkContractAttachment().withUploadResource(uploadResource);
        attachments.add(attachment);
      }
      
      hrService.saveWorkContractAttachment(client, company, entity.getId(), attachments);
      assertInCollection(attachments.get(0), hrService.findWorkContractAttachment(client, company, entity.getId()));
      return this;
    }
  }
  
  //Company Work Contract
  public class CompanyWorkPositionContractAssert extends CompanyEntityAssert<WorkPositionContract> {
    public CompanyWorkPositionContractAssert(ClientInfo client, Company company, WorkPositionContract workPositionContract) {
      super(client, company, workPositionContract);
      this.methods = new EntityServiceMethods() {
        public WorkPositionContract load() {
          return hrService.getWorkPositionContract(client, company, workPositionContract.getCode());
        }

        public WorkPositionContract save(WorkPositionContract entity) {
          return hrService.saveWorkPositionContract(client, company, entity);
        }

        public List<?> search(SqlQueryParams params) {
          params.addParam("employeeWorkContractCode", workPositionContract.getEmployeeWorkContractCode());
          return hrService.searchWorkPositionContracts(client, company, params);
        }

        public boolean archive() {
          return hrService.changeWorkPositionContractStorageState(client, company, createArchivedStorageRequest(entity));
        }
      };
    }
  }

  //Company Work Allowance
  public class CompanyWorkAllowanceAssert extends CompanyEntityAssert<WorkAllowance> {
    public CompanyWorkAllowanceAssert(ClientInfo client, Company company, WorkAllowance allowance) {
      super(client, company, allowance);
      this.methods = new EntityServiceMethods() {
        public WorkAllowance load() {
          return hrService.getWorkAllowanceByCode(client, company, allowance.getCode());
        }

        public WorkAllowance save(WorkAllowance entity) {
          return hrService.saveWorkAllowance(client, company, entity);
        }

        public List<?> search(SqlQueryParams params) {
          return hrService.searchWorkAllowances(client, company, params);
        }

        public boolean archive() {
          return hrService.changeWorkAllowancesStorageState(client, company, createArchivedStorageRequest(entity));
        }
      };
    }
  }

  //Company Work Allowance
  public class CompanyWorkPossitionAssert extends CompanyEntityAssert<WorkPosition> {
    public CompanyWorkPossitionAssert(ClientInfo client, Company company, WorkPosition possition,
        String departmentName) {
      super(client, company, possition);
      this.methods = new EntityServiceMethods() {
        public WorkPosition load() {
          return hrService.getWorkPosition(client, company, possition.getId());
        }

        public WorkPosition save(WorkPosition entity) {
          return hrService.saveWorkPosition(client, company, entity);
        }

        public List<?> search(SqlQueryParams params) {
          params.addParam("department", departmentName);
          return hrService.searchWorkPositions(client, company, params);
        }

        public boolean archive() {
          return hrService.changeWorkPositionsStorageState(client, company, createArchivedStorageRequest(entity));
        }
      };
    }
  }

  //Company Work Contract Term
  public class CompanyWorkContractTermAssert extends CompanyEntityAssert<WorkContractTerm> {
    public CompanyWorkContractTermAssert(ClientInfo client, Company company, WorkContractTerm term) {
      super(client, company, term);
      this.methods = new EntityServiceMethods() {
        public List<?> search(SqlQueryParams params) {
          return hrService.searchWorkContractTerms(client, company, createSearchQuery(term.getCategory()));
        }
      };
    }
  }

  //Company Work Term
  public class CompanyWorkTermAssert extends CompanyEntityAssert<WorkTerm> {
    public CompanyWorkTermAssert(ClientInfo client, Company company, WorkTerm term) {
      super(client, company, term);
      this.methods = new EntityServiceMethods() {
        public List<?> search(SqlQueryParams params) {
          return hrService.searchWorkTerms(client, company, params);
        }
      };
    }
  }

}