package com.devteam.module.company.service.hr.http;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpSession;

import com.devteam.core.module.data.db.SqlMapRecord;
import com.devteam.core.module.data.db.entity.ChangeStorageStateRequest;
import com.devteam.core.module.data.db.query.SqlQueryParams;
import com.devteam.core.module.http.rest.RestResponse;
import com.devteam.core.module.http.rest.v1.AuthenticationService;
import com.devteam.module.company.service.hr.HRService;
import com.devteam.module.company.service.hr.entity.*;
import com.devteam.module.company.service.http.BaseCompanyController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@ConditionalOnBean(AuthenticationService.class)
@Api(value = "devteam", tags = { "company/hr" })
@RestController
@RequestMapping("/rest/v1.0.0/company/hr")
public class HRWorkController extends BaseCompanyController {
  @Autowired
  private HRService hrService;

  protected HRWorkController() {
    super("company", "/company/hr");
  }

  @ApiOperation(value = "Get Work Position By Code", response = WorkPosition.class)
  @GetMapping("work/position/{code}")
  public @ResponseBody
  RestResponse getWorkPositionByCode(HttpSession session, @PathVariable("code") String code) {
    Callable<WorkPosition> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.getWorkPositionByCode(ctx.getClientInfo(), ctx.getCompany(), code);
    };
    return execute(Method.GET, "work/position/code/{code}", executor);
  }

  @ApiOperation(value = "Change the work-position state", response = Boolean.class)
  @PutMapping("work/position/storage-state")
  public @ResponseBody RestResponse changeWorkPositionStorageState(HttpSession session,
      @RequestBody ChangeStorageStateRequest req) {
    Callable<Boolean> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.changeWorkPositionsStorageState(getAuthorizedClientInfo(session), ctx.getCompany(), req);
    };
    return execute(Method.PUT, "work/position/storage-state", executor);
  }

  @ApiOperation(value = "Save Work Positions", response = WorkPosition.class)
  @PutMapping("work/position")
  public @ResponseBody RestResponse saveWorkPosition(HttpSession session, @RequestBody WorkPosition position) {
    Callable<WorkPosition> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.saveWorkPosition(ctx.getClientInfo(), ctx.getCompany(), position);
    };
    return execute(Method.PUT, "work/position", executor);
  }

  @ApiOperation(value = "Search Work Positions", responseContainer = "List", response = WorkPosition.class)
  @PostMapping("work/position/search")
  public @ResponseBody RestResponse searchWorkPositions(HttpSession session, @RequestBody SqlQueryParams params) {
    Callable<List<WorkPosition>> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.searchWorkPositions(ctx.getClientInfo(), ctx.getCompany(), params);
    };
    return execute(Method.POST, "work/position/search", executor);
  }

  @ApiOperation(value = "Get Work Allowances", responseContainer = "List", response = WorkAllowance.class)
  @PostMapping("work/allowance/search")
  public @ResponseBody RestResponse searchWorkAllowances(HttpSession session, @RequestBody SqlQueryParams params) {
    Callable<List<WorkAllowance>> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.searchWorkAllowances(ctx.getClientInfo(), ctx.getCompany(), params);
    };
    return execute(Method.POST, "work/allowance/search", executor);
  }

  @ApiOperation(value = "Change the work allowances state", response = Boolean.class)
  @PutMapping("work/allowance/storage-state")
  public @ResponseBody RestResponse changeWorkAllowancesStorageState(HttpSession session,
      @RequestBody ChangeStorageStateRequest req) {
    Callable<Boolean> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.changeWorkAllowancesStorageState(getAuthorizedClientInfo(session), ctx.getCompany(), req);
    };
    return execute(Method.PUT, "work/allowance/storage-state", executor);
  }

  @ApiOperation(value = "Get Work Allowance By Code", response = WorkAllowance.class)
  @GetMapping("work/allowance/{code}")
  public @ResponseBody RestResponse getWorkAllowanceByCode(HttpSession session, @PathVariable("code") String code) {
    Callable<WorkAllowance> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.getWorkAllowanceByCode(ctx.getClientInfo(), ctx.getCompany(), code);
    };
    return execute(Method.GET, "work/allowance/{code}", executor);
  }

  @ApiOperation(value = "Save Work Allowance", response = WorkAllowance.class)
  @PutMapping("work/allowance")
  public @ResponseBody RestResponse saveWorkAllowance(HttpSession session, @RequestBody WorkAllowance workAllowance) {
    Callable<WorkAllowance> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.saveWorkAllowance(ctx.getClientInfo(), ctx.getCompany(), workAllowance);
    };
    return execute(Method.PUT, "work/allowance", executor);
  }

  @ApiOperation(value = "Search Work Contract Terms", responseContainer = "List", response = WorkContractTerm.class)
  @PostMapping("work/contract/term/search")
  public @ResponseBody RestResponse searchWorkContractTerms(HttpSession session, @RequestBody SqlQueryParams params) {
    Callable<List<WorkContractTerm>> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.searchWorkContractTerms(ctx.getClientInfo(), ctx.getCompany(), params);
    };
    return execute(Method.POST, "work/contract/term/search", executor);
  }

  @ApiOperation(
      value = "Find contract attachments", responseContainer = "List", response = WorkContractAttachment.class)
    @GetMapping("work/contract/{contract-id}/attachments")
    public @ResponseBody RestResponse findContractAttachments( HttpSession session, @PathVariable("contract-id") Long contractId) {
      Callable<List<WorkContractAttachment>> executor = () -> {
        ClientContext ctx = getClientContext(session);
        return hrService.findWorkContractAttachment(ctx.getClientInfo(), ctx.getCompany(), contractId);
      };
      return execute(Method.GET, "work/contract/{contract-id}/attachments", executor);
    }

    @ApiOperation(value = "Save contract attachments", responseContainer = "List", response = WorkContractAttachment.class)
    @PutMapping("work/contract/{contract-id}/attachments")
    public @ResponseBody RestResponse saveWorkflowTaskAttachment(
      HttpSession session, @PathVariable("contract-id") Long contractId, @RequestBody List<WorkContractAttachment> attachments) {
      Callable<List<WorkContractAttachment>> executor = () -> {
        ClientContext ctx = getClientContext(session);
        return hrService.saveWorkContractAttachment(ctx.getClientInfo(), ctx.getCompany(), contractId, attachments);
      };
      return execute(Method.PUT, "task/{task-id}/attachments", executor);
    }

  @ApiOperation(value = "Save Work Contract Term", response = WorkContractTerm.class)
  @PutMapping("work/contract/term")
  public @ResponseBody RestResponse saveWorkContractTerm(HttpSession session, @RequestBody WorkContractTerm term) {
    Callable<WorkContractTerm> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.saveWorkContractTerm(ctx.getClientInfo(), ctx.getCompany(), term);
    };
    return execute(Method.PUT, "work/contract/term", executor);
  }

  @ApiOperation(value = "Change the work contract term state", response = Boolean.class)
  @PutMapping("work/contract/term/storage-state")
  public @ResponseBody RestResponse changeWorkContractTermsStorageState(HttpSession session,
      @RequestBody ChangeStorageStateRequest req) {
    Callable<Boolean> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.changeWorkContractTermsStorageState(getAuthorizedClientInfo(session), ctx.getCompany(), req);
    };
    return execute(Method.PUT, "work/contract/term/storage-state", executor);
  }

  @ApiOperation(value = "Get work contract term By Id", response = WorkContractTerm.class)
  @GetMapping("work/contract/term/{id}")
  public @ResponseBody RestResponse getWorkContractTerm(HttpSession session, @PathVariable("id") Long id) {
    Callable<WorkContractTerm> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.getWorkContractTerm(ctx.getClientInfo(), ctx.getCompany(), id);
    };
    return execute(Method.GET, "work/contract/term/{id}", executor);
  }

  @ApiOperation(value = "Search Employee Contracts", responseContainer = "List", response = EmployeeWorkContract.class)
  @PostMapping("work/contract/search")
  public @ResponseBody RestResponse searchEmployeeContracts(HttpSession session, @RequestBody SqlQueryParams params) {
    Callable<List<SqlMapRecord>> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.searchEmployeeWorkContract(ctx.getClientInfo(), ctx.getCompany(), params);
    };
    return execute(Method.POST, "work/contract/search", executor);
  }

  @ApiOperation(value = "Get Work Position Contract By Code", response = WorkPositionContract.class)
  @GetMapping("work/position/contract/{code}")
  public @ResponseBody RestResponse getWorkPositionContract(HttpSession session,
      @PathVariable("code") String code) {
    Callable<WorkPositionContract> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.getWorkPositionContract(ctx.getClientInfo(), ctx.getCompany(), code);
    };
    return execute(Method.GET, "work/position/contract/{code}", executor);
  }

  @ApiOperation(value = "Save Work Position Contract", response = WorkPositionContract.class)
  @PutMapping("work/position/contract")
  public @ResponseBody RestResponse saveWorkPositionContract(HttpSession session,
      @RequestBody WorkPositionContract workPositionContract) {
    Callable<WorkPositionContract> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.saveWorkPositionContract(ctx.getClientInfo(), ctx.getCompany(), workPositionContract);
    };
    return execute(Method.PUT, "work/position/contract", executor);
  }

  @ApiOperation(value = "Search Work Position Contracts", responseContainer = "List", response = WorkPositionContract.class)
  @PostMapping("work/position/contract/search")
  public @ResponseBody RestResponse searchWorkPositionContract(HttpSession session, @RequestBody SqlQueryParams params) {
    Callable<List<SqlMapRecord>> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.searchWorkPositionContracts(ctx.getClientInfo(), ctx.getCompany(), params);
    };
    return execute(Method.POST, "work/position/contract/search", executor);
  }

  @ApiOperation(value = "Change Work Position Contracts State", response = Boolean.class)
  @PutMapping("work/position/contract/storage-state")
  public @ResponseBody RestResponse changeWorkPositionContractsStorageState(HttpSession session,
      @RequestBody ChangeStorageStateRequest req) {
    Callable<Boolean> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.changeWorkPositionContractStorageState(getAuthorizedClientInfo(session), ctx.getCompany(), req);
    };
    return execute(Method.PUT, "work/position/contract/storage-state", executor);
  }

  @ApiOperation(value = "Save Employee Contract", response = EmployeeWorkContract.class)
  @PutMapping("work/contract")
  public @ResponseBody RestResponse saveEmployeeContract(HttpSession session,
      @RequestBody EmployeeWorkContract emplContract) {
    Callable<EmployeeWorkContract> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.saveEmployeeWorkContract(ctx.getClientInfo(), ctx.getCompany(), emplContract);
    };
    return execute(Method.PUT, "work/contract", executor);
  }

  @ApiOperation(value = "Get Employee Contracts By Code", response = EmployeeWorkContract.class)
  @GetMapping("work/contract/{code}")
  public @ResponseBody RestResponse getEmployeeContract(HttpSession session, @PathVariable("code") String code) {
    Callable<EmployeeWorkContract> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.getEmployeeWorkContract(ctx.getClientInfo(), ctx.getCompany(), code);
    };
    return execute(Method.GET, "work/contract/{code}", executor);
  }

  @ApiOperation(value = "Change the employees contracts state", response = Boolean.class)
  @PutMapping("work/contract/storage-state")
  public @ResponseBody RestResponse changeEmployeeContractsStorageState(HttpSession session,
      @RequestBody ChangeStorageStateRequest req) {
    Callable<Boolean> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.changeEmployeeContractsStorageState(getAuthorizedClientInfo(session), ctx.getCompany(), req);
    };
    return execute(Method.PUT, "work/contract/storage-state", executor);
  }

  //WorkTerm
  @ApiOperation(value = "Search Work Terms", responseContainer = "List", response = WorkTerm.class)
  @PostMapping("work/term/search")
  public @ResponseBody RestResponse searchWorkTerms(HttpSession session, @RequestBody SqlQueryParams params) {
    Callable<List<WorkTerm>> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.searchWorkTerms(ctx.getClientInfo(), ctx.getCompany(), params);
    };
    return execute(Method.POST, "work/term/search", executor);
  }

  @ApiOperation(value = "Save Work Term", response = WorkTerm.class)
  @PutMapping("work/term")
  public @ResponseBody RestResponse saveWorkTerm(HttpSession session, @RequestBody WorkTerm term) {
    Callable<WorkTerm> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.saveWorkTerm(ctx.getClientInfo(), ctx.getCompany(), term);
    };
    return execute(Method.PUT, "work/term", executor);
  }

  @ApiOperation(value = "Change the work term state", response = Boolean.class)
  @PutMapping("work/term/storage-state")
  public @ResponseBody RestResponse changeWorkTermsStorageState(HttpSession session,
      @RequestBody ChangeStorageStateRequest req) {
    Callable<Boolean> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.changeWorkTermsStorageState(getAuthorizedClientInfo(session), ctx.getCompany(), req);
    };
    return execute(Method.PUT, "work/term/storage-state", executor);
  }

  @ApiOperation(value = "Get work term By Id", response = WorkTerm.class)
  @GetMapping("work/term/{id}")
  public @ResponseBody RestResponse getWorkTerm(HttpSession session, @PathVariable("id") Long id) {
    Callable<WorkTerm> executor = () -> {
      ClientContext ctx = getClientContext(session);
      return hrService.getWorkTerm(ctx.getClientInfo(), ctx.getCompany(), id);
    };
    return execute(Method.GET, "work/term/{id}", executor);
  }
}
