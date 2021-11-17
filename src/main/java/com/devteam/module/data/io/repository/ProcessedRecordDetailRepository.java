
package com.devteam.module.data.io.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.module.data.io.entity.ProcessedRecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProcessedRecordDetailRepository extends JpaRepository<ProcessedRecordDetail, Serializable> {
  
  @Query("SELECT r from ProcessedRecordDetail r "
      + "WHERE r.companyId = :companyId AND r.entryId = :entryId  AND r.groupName = :groupName  AND r.label = :label")
  ProcessedRecordDetail findByLabel(@Param("companyId") Long companyId, @Param("entryId") Long entryId, 
                                           @Param("groupName") String groupName, @Param("label") String label);

  @Query("SELECT r from ProcessedRecordDetail r "
      + "WHERE r.companyId = :companyId AND r.entryId = :entryId  AND r.groupName = :groupName")
  List<ProcessedRecordDetail> findByGroup(@Param("companyId") Long companyId, @Param("entryId") Long entryId, 
                                          @Param("groupName") String groupName);
}