
package com.devteam.module.data.io.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.module.data.io.entity.ProcessedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProcessedRecordRepository extends JpaRepository<ProcessedRecord, Serializable> {
  @Query("SELECT r from ProcessedRecord r WHERE r.companyId = :companyId AND r.entryId = :entryId")
  public List<ProcessedRecord> findByEntry(@Param("companyId") Long companyId, @Param("entryId") Long entryId) ;
}