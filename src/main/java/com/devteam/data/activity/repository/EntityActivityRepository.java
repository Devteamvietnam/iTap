
package com.devteam.data.activity.repository;

import com.devteam.data.activity.entity.EntityActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface EntityActivityRepository extends JpaRepository<EntityActivity, Serializable> {
  @Query("SELECT e FROM EntityActivity e WHERE entityTable = :table AND entityId = :entityId")
  List<EntityActivity> findByEntityId(@Param("table") String table, @Param("entityId") Long entiyId);
}