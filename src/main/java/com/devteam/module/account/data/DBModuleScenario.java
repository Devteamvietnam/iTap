package com.devteam.module.account.data;

import com.devteam.core.AbstractDBModuleScenario;
import com.devteam.core.DBScenario;
import com.devteam.core.sample.EntityDB;
import com.devteam.module.common.ClientInfo;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DBModuleScenario extends AbstractDBModuleScenario {
    public void initialize(ClientInfo client, DBScenario scenario) throws Exception {
        EntityDB.getInstance().getData(UserData.class).initialize(client);
    }
}
