package com.devteam.core.module.misc.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.devteam.core.util.error.ErrorType;
import com.devteam.core.util.error.RuntimeError;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;


import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.helper.StringHelpers;

public class HandlebarsTemplateEngine {
  private Handlebars handlebars;
  private HandlebarsTemplateLoader templateLoader ;

  public HandlebarsTemplateEngine() {
    handlebars = new Handlebars();
    handlebars.registerHelpers(StringHelpers.class);
    handlebars.registerHelpers(ConditionalHelpers.class);
    handlebars.registerHelpers(TextHelper.class);
    handlebars.registerHelpers(ListHelper.class);
    handlebars.registerHelpers(MathHelper.class);

    handlebars.registerHelper("text:tbl", new TblHelper.Tabular());
    handlebars.registerHelper("text:tbl:header", new TblHelper.Header());
    handlebars.registerHelper("text:tbl:row", new TblHelper.Row());
  }

  public HandlebarsTemplateEngine(String ...  paths) {
    this();
    
    List<TemplateLoader> loaders = new ArrayList<>();
    TemplateLoader cpLoader = new ClassPathTemplateLoader();
    cpLoader.setSuffix("");
    loaders.add(cpLoader);
    for(String path : paths) {
      TemplateLoader loader = new FileTemplateLoader(path);
      loader.setSuffix("");
      loaders.add(loader);
    }
    templateLoader = new HandlebarsTemplateLoader(loaders.toArray(new TemplateLoader[loaders.size()]));
    handlebars.with(templateLoader);
  }
  
  public Template compile(String templatePath)  {
    int lastSlashIdx = templatePath.lastIndexOf('/');
    if(lastSlashIdx > 0) {
      String baseLoc = templatePath.substring(0, lastSlashIdx + 1);
      templateLoader.getContext().setTemplatePath(baseLoc);
    } else {
      templateLoader.getContext().setTemplatePath("");
    }
    
    try {
      Template template = handlebars.compile(templatePath);
      return template;
    } catch (IOException e) {
      throw new RuntimeError(ErrorType.Unknown, e);
    }
  }
  
  public Template compileInline(String name, String template)  {
    try {
      templateLoader.getContext().setTemplatePath("");
      return handlebars.compileInline(template);
    } catch (IOException e) {
      throw new RuntimeError(ErrorType.Unknown, e);
    }
  }
}