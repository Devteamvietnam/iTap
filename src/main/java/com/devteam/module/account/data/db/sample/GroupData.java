package com.devteam.module.account.data.db.sample;

import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.JPAService;
import com.devteam.core.module.data.db.sample.PersistableEntityAssert;
import com.devteam.core.module.data.db.sample.SampleData;
import com.devteam.core.util.dataformat.DataSerializer;
import com.devteam.module.account.AccountService;
import com.devteam.module.account.entity.AccountGroup;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class GroupData extends SampleData {
  @Autowired
  private AccountService service;

  @Autowired
  private JPAService jpaService;
  
  public AccountGroup COMPANY;

  public AccountGroup EMPLOYEES;
  public AccountGroup EMPLOYEES_SALE;
  public AccountGroup EMPLOYEES_HR;
  public AccountGroup EMPLOYEES_HR_ADMIN;

  public AccountGroup PARTNERS;

  public void initialize(ClientInfo client)  {
    COMPANY       = 
        new AccountGroup("company", "Digital", "Công ty cổ phần Digital");
    COMPANY = service.createAccountGroup(client, null, COMPANY);

    EMPLOYEES = new AccountGroup("employees", "Nhân viên", "digital employee group");
    service.createAccountGroup(client, COMPANY, EMPLOYEES);
    
    EMPLOYEES_SALE = new AccountGroup("sale", "Kinh doanh", "employees sale group");
    service.createAccountGroup(client, EMPLOYEES, EMPLOYEES_SALE);
    
    EMPLOYEES_HR = 
        new AccountGroup("hr", "Nhan vien hanh chinh", "hr is a subGroup org/employees/");
    service.createAccountGroup(client, EMPLOYEES, EMPLOYEES_HR);
    
    EMPLOYEES_HR_ADMIN = new AccountGroup("admin", "Admin Hành Chính", "Admin HR");
    service.createAccountGroup(client, EMPLOYEES_HR, EMPLOYEES_HR_ADMIN);
    
    jpaService.getEntityManager().flush();
  }

  public void assertAll(ClientInfo clientInfo) throws Exception {
    AccountGroup modified = DataSerializer.JSON.clone(EMPLOYEES);
    modified.setLabel("Employees Update");
    
    AccountGroup modifiedDesc = DataSerializer.JSON.clone(EMPLOYEES);
    modifiedDesc.setDescription("Update Description");
    
    new AccountGroupAssert(clientInfo, EMPLOYEES)
    .assertCreateChild()
    .assertEntityCreated()
    //.assertEntityUpdate()
    .assertSave(modified, (updatedGroup) -> {
      Assertions.assertEquals("Employees Update", updatedGroup.getLabel());
    //}).assertSave(modifiedDesc, (updatedGroup) -> {
    // Assertions.assertEquals("Update Description", updatedGroup.getDescription());
    });
  }

  public class AccountGroupAssert extends PersistableEntityAssert<AccountGroup> {

    public AccountGroupAssert(ClientInfo clientInfo, AccountGroup group) {
      super(clientInfo, group);
      
      this.methods = new EntityServiceMethods() {
        public AccountGroup load() {
          return service.getAccountGroup(client, group.getName());
        }
        
        public AccountGroup save(AccountGroup clone) {
          service.saveAccountGroup(client, clone);
          return load();
        }
      };
    }
    
    public AccountGroupAssert assertCreateChild() {
      List<AccountGroup> childAccountGroup = 
          service.findAccountGroupChildren(client, entity.getParentId());
      assertNotNull(childAccountGroup.get(0));
      for (AccountGroup childGroup : childAccountGroup) {
        if (childGroup.getName().equals(entity.getName())) {
          assertEquals(childGroup.getParentId(), entity.getParentId());
          assertEquals(childGroup.getLabel(), entity.getLabel());
          assertEquals(childGroup.getDescription(), entity.getDescription());
          break;
        }
        Assertions.fail();
      }
      return this;
    }
  }
}
