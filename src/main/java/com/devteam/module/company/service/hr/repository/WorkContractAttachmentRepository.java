package com.devteam.module.company.service.hr.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.company.service.hr.entity.WorkContractAttachment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface WorkContractAttachmentRepository extends DataTPRepository<WorkContractAttachment, Serializable> {
  @Query("SELECT wa FROM WorkContractAttachment wa WHERE wa.companyId = :companyId AND wa.workContractId = :id")
  public List<WorkContractAttachment> findWorkContractAttachments(@Param("companyId") Long companyId, @Param("id") Long contractId);
  
  @Modifying
  @Query("DELETE FROM WorkContractAttachment wa WHERE wa.companyId = :companyId AND wa.workContractId = :id AND wa.id NOT IN(:idSet)")
  int deleteOrphan(@Param("companyId") Long companyId, @Param("id") Long contractId, @Param("idSet") List<Long> idSet);
}