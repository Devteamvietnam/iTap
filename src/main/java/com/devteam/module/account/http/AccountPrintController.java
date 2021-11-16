package com.devteam.module.account.http;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpSession;

import com.devteam.core.module.data.db.query.SqlQueryParams;
import com.devteam.core.module.http.get.StoreInfo;
import com.devteam.core.module.http.rest.RestResponse;
import com.devteam.core.module.http.rest.v1.AuthenticationService;
import com.devteam.core.module.http.rest.v1.BaseController;
import com.devteam.module.account.AccountPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="devteam", tags= {"account"})
@RestController
@ConditionalOnBean(AuthenticationService.class)
@RequestMapping("/rest/v1.0.0/account/print")
public class AccountPrintController extends BaseController {
  @Autowired
  private AccountPrintService service;

  protected AccountPrintController() {
    super("account", "/account/report");
  }

  @ApiOperation(value = "Ativities Report", responseContainer = "List", response = StoreInfo.class)
  @PostMapping("activities/{format}")
  public @ResponseBody
  RestResponse activities(
      HttpSession session, @PathVariable("format") String format, @RequestBody SqlQueryParams params) {
    Callable<StoreInfo> executor = () -> {
      return service.activities(getAuthorizedClientInfo(session), format, params);
    };
    return execute(Method.POST, "activity", executor);
  }
  
  @ApiOperation(value = "Statistics Report", responseContainer = "List", response = StoreInfo.class)
  @PostMapping("statistics/{format}")
  public @ResponseBody RestResponse  statistics(
      HttpSession session, @PathVariable("format") String format, @RequestBody SqlQueryParams params) {
    Callable<StoreInfo> executor = () -> {
      return service.statistics(getAuthorizedClientInfo(session), format, params);
    };
    return execute(Method.POST, "statistics", executor);
  }
}