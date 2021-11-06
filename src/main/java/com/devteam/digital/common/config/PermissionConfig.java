package com.devteam.digital.common.config;

import com.devteam.digital.common.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "el")
public class PermissionConfig {

    public Boolean check(String ...permissions){
        List<String> ePermissions = SecurityUtils.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ePermissions.contains("admin") || Arrays.stream(permissions).anyMatch(ePermissions::contains);
    }
}
