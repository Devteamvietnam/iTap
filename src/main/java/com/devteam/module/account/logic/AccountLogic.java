package com.devteam.module.account.logic;

import com.devteam.CachingConfig;
import com.devteam.core.DAOService;
import com.devteam.core.JPAService;
import com.devteam.module.account.entity.Account;
import com.devteam.module.account.entity.AccountType;
import com.devteam.module.account.entity.UserProfile;
import com.devteam.module.account.model.PasswordGenerator;
import com.devteam.module.account.plugin.AccountServicePlugin;
import com.devteam.module.account.repository.AccountRepository;
import com.devteam.module.account.repository.UserProfileRepository;
import com.devteam.module.common.ClientInfo;
import com.devteam.util.error.ErrorType;
import com.devteam.util.error.RuntimeError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AccountLogic extends DAOService {
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserProfileRepository userProfileRepo;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private JPAService jpaService;

    @Autowired(required = false)
    List<AccountServicePlugin> plugins = new ArrayList<>();

    public Account authenticate(ClientInfo client, String loginId, String password) {
        System.out.println("authenticate '" + loginId + "', password = '" + password + "'");
        Account account   = getModifiableAccount(client, loginId);
        if (account != null) {
            if(passwordEncoder.matches(password, account.getPassword())) {
                return account;
            }
        }
        return null;
    }
    public Account getModifiableAccount(ClientInfo client, String loginId) {
        Account account = accountRepo.getByLoginId(loginId);
        return account;
    }

    @Cacheable(value = CachingConfig.REGION_ENTITY , key= "{'" + Account.TABLE_NAME + "', #loginId}")
    public Account getAccount(ClientInfo client, String loginId) {
        Account account = accountRepo.getByLoginId(loginId);
        if(account != null) {
            jpaService.getEntityManager().detach(account);
            account.setPassword(null);
            account.setModifiable(false);
        }
        return account;
    }
    public Account getAccountById(ClientInfo clientInfo,  String loginId) {
        return accountRepo.getAccountByLoginId(loginId);
    }

    public Account save(Account account) {
        return accountRepo.save(account);
    }

    @CacheEvict(value = CachingConfig.REGION_ENTITY , key= "{'" + Account.TABLE_NAME + "', #account.loginId}")
    public Account updateAccount(ClientInfo client, Account account) {
        if(!account.isModifiable()) {
            throw new RuntimeError(ErrorType.IllegalArgument, "Account is not modifiable");
        }
        final String   loginId   = account.getLoginId();

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        for(AccountServicePlugin plugin : plugins) {
            plugin.onPreSave(client, account, false);
        }

        if(account.getAccountType() == AccountType.USER) {
            UserProfile profile = userProfileRepo.getByLoginId(loginId);
            profile.setEmail(account.getEmail());
            profile.setMobile(account.getMobile());
            profile.setFullName(account.getFullName());
            userProfileRepo.save(profile);
        }
        account.set(client);
        account = accountRepo.save(account);

        for(AccountServicePlugin plugin : plugins) {
            plugin.onPostSave(client, account, false);
        }
        return account;
    }
}
