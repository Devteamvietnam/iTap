package com.devteam.core.module.misc.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devteam.core.util.bean.BeanInspector;
import com.devteam.core.util.ds.Objects;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;


public enum ListHelper implements Helper<Object> {
  list {
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
    FUNCTIONS.put("has", (options) -> {
      Object a = options.param(0, null);
      Objects.assertNotNull(a, "Second parameter is null");
      Object b = options.param(1, null);
      Objects.assertNotNull(b, "Third parameter is null");
      Object c = options.param(2, null);
      Objects.assertNotNull(c, "Fourth parameter is null");

      List<? extends Object> list = (List<? extends Object>) a;
      String field = b.toString();
      String value = c.toString();
      boolean result = false;
      for (Object ele : list) {
        BeanInspector<Object> inspector = BeanInspector.get(ele.getClass());
        if (value.equals(inspector.getValue(ele, field).toString())) {
          result = true;
        }
      }
      if (options.tagType == TagType.SECTION) {
        return result ? options.fn() : options.inverse();
      }
      return result ? options.hash("yes", true) : options.hash("no", false);
    });
  };
}
