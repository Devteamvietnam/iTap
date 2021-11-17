package com.devteam.module.data.io.db.sample;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.AbstractDBModuleScenario;
import com.devteam.core.module.data.db.DBScenario;
import com.devteam.core.module.data.db.sample.EntityDB;
import com.devteam.module.company.core.data.db.sample.CompanyData;
import com.devteam.module.company.core.entity.Company;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DBModuleScenario extends AbstractDBModuleScenario {
  public void initialize(ClientInfo client, DBScenario scenario) throws Exception {
    Company company = EntityDB.getInstance().getData(CompanyData.class).TEST_COMPANY;
  }
}