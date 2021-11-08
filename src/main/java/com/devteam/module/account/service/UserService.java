package com.devteam.module.account.service;

import com.devteam.module.account.entity.Role;
import com.devteam.module.account.entity.User;

import java.util.List;

public interface  UserService  {
     User saveUser(User user);
     Role saveRole(Role role);
     void addRoleToUser(String username, String name);
     User getUser(String username);
     List<User> getUsers();
}
