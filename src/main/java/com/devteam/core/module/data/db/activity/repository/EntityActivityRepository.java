
package com.devteam.core.module.data.db.activity.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.core.module.data.db.activity.entity.EntityActivity;
import com.devteam.core.module.data.db.repository.DataTPRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityActivityRepository extends DataTPRepository<EntityActivity, Serializable> {
  @Query("SELECT e FROM EntityActivity e WHERE entityTable = :table AND entityId = :entityId")
  List<EntityActivity> findByEntityId(@Param("table") String table, @Param("entityId") Long entiyId);
}