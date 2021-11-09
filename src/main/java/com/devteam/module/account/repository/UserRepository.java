package com.devteam.module.account.repository;

import com.devteam.module.account.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsUserByUsername(String username);

    UserEntity findByUsernameAndPassword(String username, String password);

    UserEntity findByUsername(String username);

    Boolean existsByEmail(String email);
}
