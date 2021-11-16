package com.devteam.core.module.misc.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.devteam.core.util.ds.Objects;
import com.devteam.core.util.text.TabularFormater;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Options.Buffer;


public interface TblHelper {
  static public class Tabular implements Helper<Object> {
    @Override
    public Object apply(final Object context, final Options options) throws IOException {
      TabularFormater tabular = new TabularFormater();
      Context newContext = Context.newContext(options.context, tabular);
      options.context.data("_CURRENT_TBL_", tabular);
      Buffer buffer = options.buffer();
      if (options.isFalsy(context)) {
        buffer.append(options.inverse(newContext));
      } else {
        buffer.append(options.fn(newContext));
      }
      buffer.append(tabular.getFormattedText());
      options.context.data("_CURRENT_TBL_", null);
      return buffer;
    }
  }

  static public class Header implements Helper<Object> {
    @Override
    public Object apply(final Object context, final Options options) throws IOException {
      TabularFormater tabular = (TabularFormater) options.context.data("_CURRENT_TBL_");
      List<String> headers = new ArrayList<>();
      headers.add(context.toString());
      for (int i = 0; i < 100; i++) {
        Object object = options.param(i, null);
        if (Objects.isNull(object)) break;
        headers.add(object.toString());
      }
      tabular.header(headers);
      return options.inverse();
    }
  }

  static public class Row implements Helper<Object> {
    @Override
    public Object apply(final Object context, final Options options) throws IOException {
      TabularFormater tabular = (TabularFormater) options.context.data("_CURRENT_TBL_");
      List<Object> cells = new ArrayList<>();
      cells.add(context.toString());
      for (int i = 0; i < 100; i++) {
        Object object = options.param(i, null);
        if (Objects.isNull(object)) break;
        cells.add(object);
      }
      tabular.addRow(cells);
      return options.inverse();
    }
  }
}
