package com.devteam.core.module.misc.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.devteam.core.module.misc.print.PDFPrintable;
import com.devteam.core.module.srpingframework.app.AppEnv;
import com.devteam.core.util.error.ErrorType;
import com.devteam.core.util.error.RuntimeError;
import com.devteam.core.util.text.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.jknack.handlebars.Template;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TemplateService {
  @Autowired
  private AppEnv appEnv;

  private HandlebarsTemplateEngine engine ;

  @Value("${app.config.template-dir:#{null}}")
  private String additionalTemplateDir;

  @Value("${app.storage.dir:#{null}}")
  private String storageDir ;
  
  public TemplateService() {}
  
  @PostConstruct
  public void onInit() {
    log.info("onInit()");
    List<String> templateDirs = new ArrayList<>();
    if(this.additionalTemplateDir != null) {
      log.info("Template Dir " + additionalTemplateDir);
      templateDirs.add(additionalTemplateDir);
    }
    for(String addon : appEnv.getAddons()) {
      String templateDir = appEnv.addonPath(addon, "templates");
      log.info("Template Dir " + templateDir);
      templateDirs.add(templateDir);
    }
    engine = new HandlebarsTemplateEngine(templateDirs.toArray(new String[templateDirs.size()]));
  }

  public void register(String templateName) {
    int idx = templateName.lastIndexOf('.');
    String id = templateName.substring(0, idx);
    register(id, templateName);
  }
  
  public void register(String id, String resourceName) {
  }
  
  public byte[] render(String templatePath, String type, Object model) {
    Template template = engine.compile(templatePath);
    if("pdf".equals(type)) return getPDFAsBytes(template, model);
    return getTextAsBytes(template, model);
  }

  public String renderAsText(String templatePath, Object model) {
    Template template = engine.compile(templatePath);
    return apply(template, model);
  }

  public byte[] render(String templateId, String templateStr, String type, Object model) {
    Template template = engine.compileInline(templateId, templateStr);
    if("pdf".equals(type)) return getPDFAsBytes(template, model);
    return getTextAsBytes(template, model);
  }

  byte[] getTextAsBytes(Template template, Object scopes) {
    return apply(template, scopes).getBytes(StringUtil.UTF8);
  }

  byte[] getPDFAsBytes(Template template, Object context) {
    PDFPrintable printable = new PDFPrintable("print/fonts");
    return printable.createPDF(apply(template, context));
  }
  
  String apply(Template template, Object context) {
    try {
      return template.apply(context);
    } catch (IOException e) {
      throw new RuntimeError(ErrorType.Unknown, e);
    }
  }
}
