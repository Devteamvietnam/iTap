package com.devteam.module.account.entity;

import com.devteam.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class BaseProfile extends BaseEntity<String> {
    @Column(name="login_id", unique =true)
    protected String loginId;
    protected String email;
    protected String mobile;
    @Column(name = "avatar_url")
    protected String avatarUrl;

}
