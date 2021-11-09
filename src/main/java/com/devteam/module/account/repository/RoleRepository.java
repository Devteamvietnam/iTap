package com.devteam.module.account.repository;

import com.devteam.module.account.entity.ERole;
import com.devteam.module.account.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    Optional<RoleEntity> findByName(ERole name);
}

