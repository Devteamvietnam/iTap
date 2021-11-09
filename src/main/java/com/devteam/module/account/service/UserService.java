package com.devteam.module.account.service;

import com.devteam.config.GeneralConfig;
import com.devteam.module.account.entity.ERole;
import com.devteam.module.account.entity.RoleEntity;
import com.devteam.module.account.entity.UserEntity;
import com.devteam.module.account.repository.RoleRepository;
import com.devteam.module.account.repository.UserRepository;
import com.google.common.io.BaseEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService implements DataInitializationService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    PasswordEncoder encoder;

    public UserEntity find(String username, String password) {
        UserEntity u = userRepo.findByUsernameAndPassword(username, password);
        if(u!=null) {
            return userRepo.findByUsernameAndPassword(username, password);
        } else {
            return null;
        }
    }

    public long count() {
        return userRepo.count();
    }
    public boolean exists(String username) {
        return userRepo.existsUserByUsername(username);
    }

    public List<UserEntity> findAll() {
        return userRepo.findAll();
    }
    public UserEntity findById(String id) {
        Optional<UserEntity> userEntity = userRepo.findById(id);
        if(userEntity.isPresent()) {
            return userEntity.get();
        } else {
            return null;
        }
    }
    private Set<RoleEntity> getRoles(Set<String> strRoles) {
        Set<RoleEntity> roles = new HashSet<RoleEntity>();

        if (strRoles == null) {
            RoleEntity userRole = roleRepo.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleEntity adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        RoleEntity modRole = roleRepo.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        RoleEntity userRole = roleRepo.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        return roles;
    }

    public void delete(String id) {
        userRepo.deleteById(id);
    }


    public void deleteAll() {
        userRepo.deleteAll();
    }

    public UserEntity find(String username) {
        return userRepo.findByUsername(username);
    }

    public UserEntity save(UserEntity en) {
        return userRepo.save(en);
    }


    @Override
    public void init() {
        if(roleRepo.count()==0) {
            RoleEntity role = new RoleEntity();
            role = new RoleEntity();
            role.setName(ERole.ROLE_USER);
            roleRepo.save(role);
            role = new RoleEntity();
            role.setName(ERole.ROLE_ADMIN);
            roleRepo.save(role);
            role = new RoleEntity();
            role.setName(ERole.ROLE_MODERATOR);
            roleRepo.save(role);
        }

        generateUser("admin", "admin", "devteamvietnam@gmail.com", Collections.singleton("ROLE_ADMIN"));
    }

    public void generateUser(String username, String password,  String email, Set<String> roles ) {

        UserEntity user = new UserEntity();
        String pwd = encoder.encode(password);
        user.setPassword(pwd);
        user.setUserRoles(getRoles(roles));
        user.setUsername(username);
        user.setEmail(email);
        userRepo.save(user);

    }
}
