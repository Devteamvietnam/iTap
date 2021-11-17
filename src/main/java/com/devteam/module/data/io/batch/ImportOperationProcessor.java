package com.devteam.module.data.io.batch;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.util.dataformat.DataSerializer;
import com.devteam.module.company.core.CompanyService;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.data.io.DataImportLogic;
import com.devteam.module.data.io.ImportEntryOp;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class ImportOperationProcessor implements ItemProcessor<ImportEntryOp, ImportEntryOp> {
  @Autowired
  private CompanyService companyService;
  
  @Autowired
  ApplicationContext context;
  
  @Override
  public ImportEntryOp process(ImportEntryOp operation) throws Exception {
    System.out.println("Process Operation................");
    System.out.println(DataSerializer.JSON.toString(operation));
    
    DataImportLogic logic = context.getBean(DataImportLogic.class);
    Company company = companyService.getCompany(ClientInfo.DEFAULT, operation.getCompanyCode());
    logic.process(ClientInfo.DEFAULT, company, operation);
    return operation;
  }
}
