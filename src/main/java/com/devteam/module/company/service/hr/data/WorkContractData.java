package com.devteam.module.company.service.hr.data;


import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.SqlMapRecord;
import com.devteam.core.module.data.db.sample.EntityDB;
import com.devteam.core.util.dataformat.DataSerializer;
import com.devteam.core.util.ds.AssertTool;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.company.service.hr.entity.ContractStatus;
import com.devteam.module.company.service.hr.entity.EmployeeWorkContract;
import com.devteam.module.company.service.hr.entity.EmployeeWorkTerm;
import com.devteam.module.company.service.hr.entity.WorkPositionContract;

import java.util.ArrayList;

public class WorkContractData extends DBModuleDataAssert{
  public  EmployeeWorkContract WORK_CONTRACT_ADMIN;
  private EmployeeWorkContract WORK_CONTRACT_THIEN;
  private EmployeeWorkContract[] ALL_WORK_CONTRACT;
  
  private WorkPositionContract WORK_POSITION_CONTRACT_TUAN_APR;
  private WorkPositionContract WORK_POSITION_CONTRACT_TUAN_AUG;
  
  protected void initialize(ClientInfo client, Company company) {
    initContract(client, company);
    initWorkPositionContract(client, company);
  }
  
  protected void initContract(ClientInfo client, Company company) {
    HRData         HR       = EntityDB.getInstance().getData(HRData.class);
    EmployeeData   EMPLOYEE = EntityDB.getInstance().getData(EmployeeData.class);
   
    WORK_CONTRACT_ADMIN =
        new EmployeeWorkContract(EMPLOYEE.THIEN)
        .withSatatus(ContractStatus.ACTIVE)
        .withWorkTerm(
            new EmployeeWorkTerm(HR.WORK_TERM_PROVISION),
            new EmployeeWorkTerm(HR.WORK_TERM_REGULATION))
        .withWorkPositionContract(
            new WorkPositionContract("Thien January", "tuan-jan", EMPLOYEE.THIEN, HR.WORK_POSITION_IT_DEVELOPER)
            .withSalary(7_000_000)
            .withSocialInsuranceSalary(7_000_000)
            .withReportTo(EMPLOYEE.ADMIN)
            .withAnnualTakeOff(17)
            .withAnnualSickLeave(35));
    
    WORK_CONTRACT_THIEN =
        new EmployeeWorkContract(EMPLOYEE.THIEN)
        .withSatatus(ContractStatus.ACTIVE)
        .withWorkTerm(
            new EmployeeWorkTerm(HR.WORK_TERM_PROVISION),
            new EmployeeWorkTerm(HR.WORK_TERM_REGULATION))
        .withWorkPositionContract(
            new WorkPositionContract("Hieu January", "hieu-jan", EMPLOYEE.THIEN, HR.WORK_POSITION_IT_DEVELOPER)
            .withAnnualTakeOff(12)
            .withAnnualSickLeave(30)
            .withSalary(9_000_000)
            .withSocialInsuranceSalary(9_000_000)
            .withReportTo(EMPLOYEE.THIEN));

    
    ALL_WORK_CONTRACT = new EmployeeWorkContract[] {
     WORK_CONTRACT_THIEN
    };
    
    for(EmployeeWorkContract sel : ALL_WORK_CONTRACT) {
      sel = hrService.saveEmployeeWorkContract(client, company, sel);
    }
  }
  
  protected void initWorkPositionContract(ClientInfo client, Company company) {
    HRData HR = EntityDB.getInstance().getData(HRData.class);
    EmployeeData EMPLOYEE = EntityDB.getInstance().getData(EmployeeData.class);
    
    WORK_POSITION_CONTRACT_TUAN_APR = 
        new WorkPositionContract("Thien April", "thien-apr", EMPLOYEE.THIEN, HR.WORK_POSITION_IT_DEVELOPER)
        .withEmployeeWorkContract(WORK_CONTRACT_THIEN)
        .withSalary(9_000_000)
        .withSocialInsuranceSalary(9_000_000)
        .withReportTo(EMPLOYEE.THIEN)
        .withAnnualTakeOff(22)
        .withAnnualSickLeave(30);
    WORK_POSITION_CONTRACT_TUAN_APR = hrService.saveWorkPositionContract(client, company, WORK_POSITION_CONTRACT_TUAN_APR);
  }

  public void assertCompanyEmployeeWorkContract() {
    new CompanyEmployeeWorkContractAssert(client, company, WORK_CONTRACT_THIEN)
    .assertLoad((origin, entity) -> {
      AssertTool.assertNotNull(entity.getCode());
      AssertTool.assertEquals(WORK_CONTRACT_THIEN.getCode(), entity.getCode());
    })
    .assertSave(WORK_CONTRACT_THIEN, (saved) -> {
    	AssertTool.assertEquals(WORK_CONTRACT_THIEN.getCode(), saved.getCode());
    })
    .assertEntitySearch((entity, list) -> {
      SqlMapRecord rec = (SqlMapRecord) list.get(0);
      AssertTool.assertEquals(entity.getCode(), rec.get("code"));
    })
    .assertEntityArchive();
  }
  
  public void assertCompanyWorkPositionContract() {
    String code = WORK_POSITION_CONTRACT_TUAN_APR.getCode();
    WorkPositionContract modifield = DataSerializer.JSON.clone(WORK_POSITION_CONTRACT_TUAN_APR);
    modifield.setAllowances(new ArrayList<>());
    new CompanyWorkPositionContractAssert(client, company, modifield)
    .assertLoad((origin, entity) -> {
      AssertTool.assertNotNull(entity.getCode());
      AssertTool.assertEquals(code, entity.getCode());
    })
    .assertSave(modifield, (saved) -> {
      AssertTool.assertEquals(code, saved.getCode());
    })
    .assertEntitySearch((entity, list) -> {
      SqlMapRecord rec = (SqlMapRecord) list.get(0);
      AssertTool.assertEquals(entity.getCode(), rec.get("code"));
      AssertTool.assertEquals(entity.getEmployeeWorkContractCode(), rec.get("employeeWorkContractCode"));
    })
    .assertEntityArchive();
  }
}