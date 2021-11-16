package com.devteam.core.module.misc.template;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.devteam.core.util.ds.Objects;
import com.devteam.core.util.text.DateUtil;
import com.devteam.core.util.text.TextUtil;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;


public enum TextHelper implements Helper<Object> {
  text {
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
    FUNCTIONS.put("eq", (options) -> {
      Object a = options.param(0, null);
      Objects.assertNotNull(a, "First parameter is null");
      Object b = options.param(1, null);
      Objects.assertNotNull(b, "Second parameter is null");

      String s1 = a.toString();
      String s2 = b.toString();
      boolean result = s1.equals(s2);
      if (options.tagType == TagType.SECTION) {
        return result ? options.fn() : options.inverse();
      }
      return result ? options.hash("yes", true) : options.hash("no", false);
    });

    FUNCTIONS.put("ft:currency", (options) -> {
      Object a = options.param(0, null);
      Objects.assertNotNull(a, "First parameter is null");
      return TextUtil.asHummanReadableCurrency3Precision(Double.parseDouble(a.toString()));
    });

    FUNCTIONS.put("ft:date", (options) -> {
      Object a = options.param(0, null);
      Objects.assertNotNull(a, "First parameter is null");
      try {
        return DateUtil.asCompactDate(DateUtil.ICT_DATE_TIME.parse(a.toString()));
      } catch (ParseException e) {
        throw new IllegalArgumentException("Wrong date time format");
      }
    });

    FUNCTIONS.put("ft:date:time", (options) -> {
      Object a = options.param(0, null);
      Objects.assertNotNull(a, "First parameter is null");
      try {
        return DateUtil.asCompactDate(DateUtil.ICT_DATE_TIME.parse(a.toString()));
      } catch (ParseException e) {
        throw new IllegalArgumentException("Wrong date time format");
      }
    });
  }
}
