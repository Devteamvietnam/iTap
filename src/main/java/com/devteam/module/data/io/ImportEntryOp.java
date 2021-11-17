package com.devteam.module.data.io;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class ImportEntryOp {
  static public enum ImportEntryOpType { Validate, Import, Remove }
  static public enum ImportEntryOpMode { Skip, Override }
  
  private String            companyCode;
  private String            module;
  private String            plugin;
  private ImportEntryOpType type = ImportEntryOpType.Validate;
  private ImportEntryOpMode mode = ImportEntryOpMode.Override;
  private String            entryName;
  
  public ImportEntryOp(String companyCode, String module, String plugin, String entryName) {
    this.companyCode = companyCode;
    this.module = module;
    this.plugin = plugin;
    this.entryName = entryName;
  }
}
