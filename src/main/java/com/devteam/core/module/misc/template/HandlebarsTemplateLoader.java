package com.devteam.core.module.misc.template;

import java.io.IOException;

import com.github.jknack.handlebars.io.CompositeTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;

import lombok.Getter;
import lombok.Setter;

public class HandlebarsTemplateLoader extends CompositeTemplateLoader {
  private ThreadLocal<Context> threadLocal = new ThreadLocal<>() {
    @Override protected Context initialValue() {
      return new Context();
    }
  };

  public HandlebarsTemplateLoader(final TemplateLoader... loaders) {
    super(loaders);
  }
  
  public Context getContext() { return threadLocal.get(); }
  
  @Override
  public TemplateSource sourceAt(String location) throws IOException {
    if(location.charAt(0) == '.' ) {
      if(location.startsWith("./")) {
        Context context = threadLocal.get();
        location = location.replace("./", context.templatePath);
      } else if(location.startsWith("..")) {
        Context context = threadLocal.get();
        location = context.templatePath + "/" + location;
      } 
    }
    return super.sourceAt(location);
  }
  
  @Override
  public String resolve(String location) {
    if(location.startsWith("./")) {
      Context context = threadLocal.get();
      location = location.replace("./", context.templatePath);
    }
    return super.resolve(location);
  }
  
  @Getter @Setter
  static public class Context {
    private String templatePath = "";
  }
}
