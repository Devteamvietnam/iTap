package com.devteam.module.account.entity;

import com.devteam.config.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.*;


@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;
    @Column(unique = true, length = 128, nullable = false)
    private String email;
    @Column(unique = true)
    private String username;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 512)
    private String avatar;
    private Boolean status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties
    private Collection<Role> roles = new ArrayList<>();

    public User(String name, String email, String username, String password, String avatar, boolean status) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.status = status;
    }
}
