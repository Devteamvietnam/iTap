package com.devteam.core.module.misc.http;

import javax.servlet.http.HttpServletRequest;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.entity.ICompany;
import com.devteam.core.module.http.rest.v1.AuthenticationService;
import com.devteam.core.module.http.rest.v1.BaseController;
import com.devteam.core.module.http.session.ClientSession;
import com.devteam.core.module.misc.template.TemplateService;
import com.devteam.core.util.ds.MapObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



@ConditionalOnBean(AuthenticationService.class)
@Api(value = "devteam", tags = { "misc", "template" })
@RestController
@RequestMapping("/rest/v1.0.0/template")
public class TemplateController extends BaseController {

  @Autowired
  private TemplateService service;

  protected TemplateController() {
    super("misc", "template");
  }
  
  @ApiOperation(value = "Generate Template", response = ResponseEntity.class)
  @GetMapping("public/{idPrefix}/**")
  public ResponseEntity<Resource> handlePublic(HttpServletRequest request, @PathVariable("idPrefix") String idPrefix)  {
    String templateId = parseEndRequestUrl(request, idPrefix); 
    MapObject params = createParams(request);
    String type = params.getString("_type", "pdf");
    boolean download = params.getBoolean("_download", false);
    byte[] data = service.render(templateId, type, params);
    return createResource(templateId, "application/pdf",  data, download);
  }
  
  @ApiOperation(value = "Generate Template", response = ResponseEntity.class)
  @GetMapping("{idPrefix}/**")
  public ResponseEntity<Resource> getHandle(HttpServletRequest request, @PathVariable("idPrefix") String idPrefix)  {
    ClientSession clientSession = getAuthorizedClientSession(request.getSession(false));
    ClientInfo client = clientSession.getClientInfo();
    ICompany company = clientSession.getBean(ICompany.class);
    
    String templateId = parseEndRequestUrl(request, idPrefix); 
    MapObject params = createParams(request);
    params.put(client);
    params.put(ICompany.class, company);
    String type = params.getString("_type", "pdf");
    boolean download = params.getBoolean("_download", false);
    byte[] data = service.render(templateId, type, params);
    return createResource(templateId, "application/pdf",  data, download);
  }
}