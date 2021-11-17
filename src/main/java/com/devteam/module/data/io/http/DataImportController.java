package com.devteam.module.data.io.http;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpSession;

import com.devteam.core.module.http.rest.RestResponse;
import com.devteam.core.module.http.rest.v1.AuthenticationService;
import com.devteam.core.module.http.upload.UploadResourceRequest;
import com.devteam.module.company.service.http.BaseCompanyController;
import com.devteam.module.data.io.DataIOPluginInfo;
import com.devteam.module.data.io.DataIOService;
import com.devteam.module.data.io.ImportEntryProcessedResult;
import com.devteam.module.data.io.entity.DataIOPluginImportEntries;
import com.devteam.module.data.io.entity.ProcessedRecordDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@ConditionalOnBean(AuthenticationService.class)
@Api(value = "devteam", tags = {"data/import"})
@RestController
@RequestMapping("/rest/v1.0.0/data/import")
public class DataImportController extends BaseCompanyController {
  @Autowired
  private DataIOService service;

  protected DataImportController() {
    super("data", "data/import");
  }

  @ApiOperation(value = "Get all the support data plugin", responseContainer="list", response = DataIOPluginInfo.class)
  @GetMapping("plugin/all")
  public @ResponseBody RestResponse findPlugins(HttpSession session) {
    Callable<List<DataIOPluginInfo>> executor = () -> {
      BaseCompanyController.ClientContext ctx = getClientContext(session);
      return service.getPlugins(ctx.getClientInfo(), ctx.getCompany());
    };
    return execute(Method.PUT, "import/type/all", executor);
  }

  @ApiOperation(value = "Get the support data plugin by module and pluginName", response = DataIOPluginInfo.class)
  @GetMapping("plugin/{module}/{pluginName}")
  public @ResponseBody RestResponse findPlugins(HttpSession session, @PathVariable("module") String module, @PathVariable("pluginName") String pluginName) {
    Callable<DataIOPluginInfo> executor = () -> {
      BaseCompanyController.ClientContext ctx = getClientContext(session);
      return service.getPlugin(ctx.getClientInfo(), ctx.getCompany(), module, pluginName);
    };
    return execute(Method.PUT, "import/type/{module}/{pluginName}", executor);
  }
  
  @ApiOperation(value = "Get all the support data plugin", response = DataIOPluginImportEntries.class)
  @GetMapping("{module}/{name}")
  public @ResponseBody RestResponse getImportDescriptor(
      HttpSession session, @PathVariable("module") String module, @PathVariable("name") String name) {
    Callable<DataIOPluginImportEntries> executor = () -> {
      BaseCompanyController.ClientContext ctx = getClientContext(session);
      return service.getImportEntries(ctx.getClientInfo(), ctx.getCompany(), module, name);
    };
    return execute(Method.GET, "{moudle}/{name}", executor);
  }

  @ApiOperation(value = "Get processed results", response = ImportEntryProcessedResult.class)
  @GetMapping(value="{module}/{pluginName}/{entryName}/results")
  public @ResponseBody RestResponse getProcessedResults(
      HttpSession session, @PathVariable("module") String module, @PathVariable("pluginName") String pluginName,
      @PathVariable("entryName") String entryName) {
    Callable<ImportEntryProcessedResult> executor = () -> {
      BaseCompanyController.ClientContext ctx = getClientContext(session);
      ImportEntryProcessedResult results = 
          service.getImportEntryProcessedResult(ctx.getClientInfo(), ctx.getCompany(), module, pluginName, entryName);
      return results;
    };
    return execute(Method.GET, "{moudle}/{name}/results/{entryName}", executor);
  }
  
  @ApiOperation(value = "Get processed record details", response = ProcessedRecordDetail.class)
  @GetMapping(value = "{module}/{pluginName}/{entryName}/{groupName}/{label}")
  public @ResponseBody RestResponse getProcessedRecordDetail(
      HttpSession session, @PathVariable("module") String module, @PathVariable("pluginName") String pluginName,
      @PathVariable("entryName") String entryName, @PathVariable("groupName") String groupName, @PathVariable("label") String label) {
    Callable<ProcessedRecordDetail> executor = () -> {
      BaseCompanyController.ClientContext ctx = getClientContext(session);
       ProcessedRecordDetail recordDetail = 
           service.getProcessedRecordDetail(ctx.getClientInfo(), ctx.getCompany(), module, pluginName, entryName, groupName, label);
       return recordDetail;
    };
    return execute(Method.GET, "{moudle}/{pluginName}/{entryName}/{groupName}/{label}", executor);
  }

  @ApiOperation(value = "Upload user resources", responseContainer = "List", response = DataIOPluginImportEntries.class)
  @PutMapping("{module}/{pluginName}")
  public RestResponse userUpload(
      HttpSession session, 
      @PathVariable("module") String module, @PathVariable("pluginName") String pluginName, 
      @RequestBody UploadResourceRequest req) {
    Callable<DataIOPluginImportEntries> executor = () -> {
      BaseCompanyController.ClientContext ctx = getClientContext(session);
      return service.upload(ctx.getClientInfo(), ctx.getCompany(), module, pluginName, req);
    };
    return execute(Method.PUT, "{moudle}/{name}", executor);
  }
  
  @ApiOperation(value = "Remove user resources upload", responseContainer = "List", response = DataIOPluginImportEntries.class)
  @DeleteMapping("{module}/{pluginName}")
  public RestResponse removeUserUpload(
      HttpSession session, 
      @PathVariable("module") String module, @PathVariable("pluginName") String pluginName, 
      @RequestBody UploadResourceRequest req) {
    Callable<DataIOPluginImportEntries> executor = () -> {
      BaseCompanyController.ClientContext ctx = getClientContext(session);
      return service.remove(ctx.getClientInfo(), ctx.getCompany(), module, pluginName, req);
    };
    return execute(Method.PUT, "{moudle}/{name}", executor);
  }
}

