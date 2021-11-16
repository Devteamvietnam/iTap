package com.devteam.core.module.misc.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.devteam.core.util.ds.Objects;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;


public enum MathHelper implements Helper<Object> {

  math {
    @Override public Object apply(final Object a, final Options options) throws IOException {
      Objects.assertNotNull(a, "Function name is null");
      String functionName = a.toString();

      Function function = FUNCTIONS.get(functionName);
      if(function != null) {
        return function.apply(options);
      }

      throw new IllegalArgumentException("Wrong function name");
    }
  };

  static Map<String, Function> FUNCTIONS = new HashMap<>();  
  static {
    FUNCTIONS.put("inc", (options) -> {
      Object a = options.param(0, null);
      Objects.assertTrue(Integer.class.isInstance(a), "Expect Integer type");
      int incAmount = 1;
      Object b = options.param(1, null);
      if(b != null) {
        Objects.assertTrue(Integer.class.isInstance(b), "Expect Integer type");
        incAmount = (int) b;
      }
      int value = (int) a;
      return value +  incAmount;
    });
  };
}