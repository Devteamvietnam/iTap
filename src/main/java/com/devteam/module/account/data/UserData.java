package com.devteam.module.account.data;

import com.devteam.module.account.entity.AccountType;
import com.devteam.module.account.entity.UserProfile;
import com.devteam.module.account.model.NewAccountModel;
import com.devteam.module.account.service.AccountService;
import com.devteam.module.common.ClientInfo;
import com.devteam.util.dataformat.DataSerializer;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class UserData extends AccountData {
    static AccountType USER         = AccountType.USER;

    @Autowired
    private AccountService accountService;

    public UserProfile THIEN;

    public void initialize(ClientInfo client) {

        THIEN =
                new UserProfile("Thien", "Thien Dinh", "thien@devteam.com").
                        withMobile("0337303666");
        THIEN = createAccount(THIEN);
    }

    private UserProfile createAccount(UserProfile profile) {
        NewAccountModel model = new NewAccountModel().withUserProfile(profile, profile.getLoginId());
        profile = accountService.createNewAccount(ClientInfo.DEFAULT, model).getProfile();
        return profile;
    }

    public void assertAll(ClientInfo client) throws Exception {
        UserProfile modifiedFullName = DataSerializer.JSON.clone(THIEN);
        modifiedFullName.setFullName("Thien Update");
        new UserProfileAssert(client, THIEN)
                .assertEntityUpdate()
                .assertSave(modifiedFullName, (updateProfile) -> {
                    Assertions.assertEquals("Thien Update", updateProfile.getFullName());
                });
    }

}
