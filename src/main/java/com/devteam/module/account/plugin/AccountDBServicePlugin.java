package com.devteam.module.account.plugin;

import com.devteam.core.DBServicePlugin;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = DBServicePlugin.ACCOUNT)
public class AccountDBServicePlugin extends DBServicePlugin {

}
