package com.devteam.security;

import com.devteam.security.entity.AccessToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor @Getter @Setter
public class ACLModel {
    private String       sessionId;
    private AccessToken accessToken;
    private AccountAclModel accountAcl;
    private List<AccountAclModel> availableAccountAcls;

    public ACLModel(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @JsonIgnore
    public boolean isSuperUser() { return "admin".equals(accessToken.getLoginId()) ; }

    public ACLModel withAcessToken(AccessToken token) {
        this.accessToken = token;
        return this;
    }

    public ACLModel withAvailableAccountAcls(List<AccountAclModel> acls) {
        this.availableAccountAcls = acls;
        if(acls.size() > 0) {
            this.accountAcl = acls.get(0);
        }
        return this;
    }
}
