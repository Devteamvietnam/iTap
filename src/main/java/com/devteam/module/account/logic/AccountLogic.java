package com.devteam.module.account.logic;

import com.devteam.core.DAOService;
import com.devteam.core.JPAService;
import com.devteam.module.account.model.PasswordGenerator;
import com.devteam.module.account.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountLogic extends DAOService {
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private JPAService jpaService;
}
