package com.devteam.security;

import com.devteam.core.enums.AccessType;
import com.devteam.security.entity.AppAccessPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountAclModel {
    private AccessType accessType = AccessType.ACCOUNT;
    private String loginId;
    private int priority;

    private List<AppAccessPermission> appPermissions;
}
