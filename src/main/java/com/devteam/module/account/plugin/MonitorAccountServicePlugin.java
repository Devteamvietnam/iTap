package com.devteam.module.account.plugin;

import com.devteam.module.account.entity.Account;
import com.devteam.module.common.ClientInfo;
import org.springframework.stereotype.Component;

@Component
public class MonitorAccountServicePlugin extends AccountServicePlugin {

    public MonitorAccountServicePlugin() {
        super("monitor");
    }

    @Override
    public void onPreSave(ClientInfo client, Account account, boolean isNew) {
    }

    @Override
    public void onPostSave(ClientInfo client, Account account, boolean isNew) {
    }

}
