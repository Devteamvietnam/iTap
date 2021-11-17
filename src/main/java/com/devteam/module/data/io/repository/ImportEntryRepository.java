
package com.devteam.module.data.io.repository;

import java.io.Serializable;
import java.util.List;

import com.devteam.module.data.io.entity.ImportEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ImportEntryRepository extends JpaRepository<ImportEntry, Serializable> {
  @Query("SELECT e from ImportEntry e WHERE e.companyId = :companyId AND e.code = :code")
  public ImportEntry getByCode(@Param("companyId") Long companyId, @Param("code") String code);
  
  @Query("SELECT e from ImportEntry e WHERE e.companyId = :companyId AND e.module = :module AND e.plugin = :plugin AND e.fileName = :entryName")
  public ImportEntry getByEntryName(
      @Param("companyId") Long companyId, 
      @Param("module") String module, @Param("plugin") String plugin, @Param("entryName") String entryName);
  
  @Query("SELECT e from ImportEntry e WHERE e.companyId = :companyId AND e.module = :module AND e.plugin = :plugin")
  public List<ImportEntry> findImportEntries(
      @Param("companyId") Long companyId, 
      @Param("module") String module, @Param("plugin") String plugin);
}