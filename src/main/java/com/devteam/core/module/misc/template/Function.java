package com.devteam.core.module.misc.template;

import java.io.IOException;

import com.github.jknack.handlebars.Options;

public interface Function {
  public Object apply(Options options) throws IOException ;
}
