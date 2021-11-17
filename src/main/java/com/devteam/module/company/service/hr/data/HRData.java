package com.devteam.module.company.service.hr.data;


import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.sample.EntityDB;
import com.devteam.core.util.ds.AssertTool;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.company.service.hr.entity.WorkAllowance;
import com.devteam.module.company.service.hr.entity.WorkContractTerm;
import com.devteam.module.company.service.hr.entity.WorkPosition;
import com.devteam.module.company.service.hr.entity.WorkTerm;

public class HRData extends DBModuleDataAssert {
  public WorkTerm WORK_TERM_REGULATION;
  public WorkTerm WORK_TERM_TRANSPORTATION;
  public WorkTerm WORK_TERM_PROVISION;
  public WorkTerm[] ALL_WORK_TERMS;
  
  public WorkAllowance WORK_ALLOWANCE_PARKING;
  public WorkAllowance WORK_ALLOWANCE_LUNCH;
  public WorkAllowance WORK_ALLOWANCE_TELEPHONE;
  public WorkAllowance WORK_ALLOWANCE_PETROL;
  public WorkAllowance WORK_ALLOWANCE_LIABILITY;
  public WorkAllowance WORK_ALLOWANCE_ATTENDANCE;
  private WorkAllowance[] ALL_WORK_ALLOWANCES;

  public WorkContractTerm WORK_CONTRACT_TERM_TIME;
  public WorkContractTerm WORK_CONTRACT_TERM_RESPON;
  public WorkContractTerm WORK_CONTRACT_TERM_INSURANCE;
  public WorkContractTerm[] ALL_WORK_CONTRACT_TERMS;
  
  public WorkPosition WORK_POSITION_IT_DEVELOPER;
  public WorkPosition WORK_POSITION_LABOR;
  public WorkPosition WORK_POSITION_SALE;
  public WorkPosition NEW_WORK_POSITION;
  private WorkPosition[] ALL_WORK_POSITIONS;

  
  protected void initialize(ClientInfo client, Company company) {
    initWorkTerm(client, company);
    initWorkAllowance(client, company);
    initWorkContractTerm(client, company);
    initWorkPosition(client, company);
  }

  private void initWorkTerm(ClientInfo client, Company company) {
    WORK_TERM_REGULATION = new WorkTerm("regulation", "Regulation", "To be provided equipments/tools depending on your concrete jobs");
    WORK_TERM_TRANSPORTATION = new WorkTerm("transportation", "Transportation means", "Self-sufficed");
    WORK_TERM_PROVISION = new WorkTerm("provision", "Provision", "The employee is entitled to annual leave of 12 days per year and on national holidays under the provisions of the State");

    ALL_WORK_TERMS = new WorkTerm[] {
      WORK_TERM_REGULATION, WORK_TERM_TRANSPORTATION, WORK_TERM_PROVISION
    };

    for (WorkTerm term: ALL_WORK_TERMS) {
      hrService.saveWorkTerm(client, company, term);
    }
  }
  private void initWorkAllowance(ClientInfo client, Company company) {
    WORK_ALLOWANCE_PARKING =
        new WorkAllowance("Parking allowance").
        withAmount(5000);

    WORK_ALLOWANCE_LUNCH = 
        new WorkAllowance("Lunch meal allowance").
        withAmount(35000);

    WORK_ALLOWANCE_TELEPHONE = 
        new WorkAllowance("Telephone allowance").
        withAmount(500000);
    
    WORK_ALLOWANCE_PETROL = 
        new WorkAllowance("Petrol allowance").
        withAmount(300000);
    
    WORK_ALLOWANCE_LIABILITY = 
        new WorkAllowance("Liablility allowance").
        withAmount(1500000);
    
    WORK_ALLOWANCE_ATTENDANCE = 
        new WorkAllowance("Attendance allowance").
        withAmount(100000);
    
    ALL_WORK_ALLOWANCES = new WorkAllowance[] {
        WORK_ALLOWANCE_LUNCH, WORK_ALLOWANCE_PARKING, WORK_ALLOWANCE_TELEPHONE , WORK_ALLOWANCE_PETROL,
        WORK_ALLOWANCE_LIABILITY, WORK_ALLOWANCE_ATTENDANCE
    };

    for(WorkAllowance workAllowance: ALL_WORK_ALLOWANCES) {
      hrService.saveWorkAllowance(client, company, workAllowance);
    }
  }

  private void initWorkContractTerm(ClientInfo client, Company company) {
    WORK_CONTRACT_TERM_TIME = new WorkContractTerm("time", "Working duration", "Work from 8h00 to 17h30");
    WORK_CONTRACT_TERM_RESPON = new WorkContractTerm("responsibility", "Ensure security", "Do not disclose company data");
    WORK_CONTRACT_TERM_INSURANCE = new WorkContractTerm("insurance", "Insurance", "social insurance is required");

    ALL_WORK_CONTRACT_TERMS = new WorkContractTerm[] {
        WORK_CONTRACT_TERM_TIME, WORK_CONTRACT_TERM_RESPON, WORK_CONTRACT_TERM_INSURANCE
    };

    for (WorkContractTerm term: ALL_WORK_CONTRACT_TERMS) {
      hrService.saveWorkContractTerm(client, company, term);
    }
  }
  
  private void initWorkPosition(ClientInfo client, Company company) {
    DepartmentData DEPT  = EntityDB.getInstance().getData(DepartmentData.class);
    WORK_POSITION_IT_DEVELOPER = 
        new WorkPosition("itdev")
        .withLabel("IT DEVELOPER")
        .withBasicSalary(4000000).withPerformanceSalary(5000000)
        .withWorkAllowance(WORK_ALLOWANCE_LUNCH).withWorkAllowance(WORK_ALLOWANCE_PARKING)
        .withHRDepartment(DEPT.IT)
        .withWorkContractTerm(WORK_CONTRACT_TERM_TIME);

    WORK_POSITION_LABOR = 
        new WorkPosition("labor")
        .withLabel("LABOR")
        .withBasicSalary(2000000).withPerformanceSalary(1000000)
        .withWorkAllowance(WORK_ALLOWANCE_LUNCH).withWorkAllowance(WORK_ALLOWANCE_TELEPHONE).withWorkAllowance(WORK_ALLOWANCE_PARKING)
        .withHRDepartment(DEPT.HR)
        .withWorkContractTerm(WORK_CONTRACT_TERM_RESPON);

    NEW_WORK_POSITION = 
        new WorkPosition("NEWWORKPOSITION").
        withLabel("NEW WORK POSITION").withBasicSalary(1000000).
        withPerformanceSalary(1000000).
        withHRDepartment(DEPT.IT);

    ALL_WORK_POSITIONS = new WorkPosition[] {
        WORK_POSITION_IT_DEVELOPER, WORK_POSITION_LABOR, WORK_POSITION_SALE
    };

    for (WorkPosition sel: ALL_WORK_POSITIONS) {
      hrService.saveWorkPosition(client, company, sel);
    }
  }


  public void assertWorkTerm() {
    new CompanyWorkTermAssert(client, company, WORK_TERM_REGULATION)
    .assertEntitySearch();
  }
  
  public void assertCompanyWorkAllowance() {
    new CompanyWorkAllowanceAssert(client, company, WORK_ALLOWANCE_ATTENDANCE)
    .assertLoad((origin, entity) -> {
      AssertTool.assertNotNull(entity.getCode());
    })
    .assertEntityUpdate()
    .assertEntitySearch()
    .assertEntityArchive();
  }
  
  public void assertCompanyWorkPossition() {
    DepartmentData DEPT  = EntityDB.getInstance().getData(DepartmentData.class);
    new CompanyWorkPossitionAssert(client, company, WORK_POSITION_IT_DEVELOPER, DEPT.IT.getName())
    .assertLoad((origin, entity) -> {
      AssertTool.assertNotNull(entity.getCode());
      AssertTool.assertEquals(2, entity.getAllowances().size());
      AssertTool.assertEquals(1, entity.getTerms().size());
    })
    .assertEntityUpdate()
    .assertEntitySearch()
    .assertEntityArchive();
  }
  
  public void assertWorkContractTerm() {
    new CompanyWorkContractTermAssert(client, company, WORK_CONTRACT_TERM_RESPON)
    .assertEntitySearch();
  }

}