package com.devteam.module.account.service;

import com.devteam.module.account.logic.AccountLogic;
import com.devteam.module.account.logic.ProfileLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("AccountService")
public class AccountService {
    @Autowired
    private AccountLogic accountLogic;

    @Autowired
    private ProfileLogic profileLogic;

    @PostConstruct
    public void onInit() {
    }

}
