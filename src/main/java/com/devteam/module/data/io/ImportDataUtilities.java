package com.devteam.module.data.io;

import java.util.Date;

import com.devteam.core.util.text.StringUtil;

public class ImportDataUtilities {
  public static String convertLabelToCode(String label, boolean isGenerateTime) {
    if(StringUtil.isEmpty(label)) {
      return "";
    }
    StringBuilder codeString = new StringBuilder(label.toLowerCase().trim().replaceAll(" ", "-"));
    if(isGenerateTime) {
      Date now = new Date();
      codeString = codeString.append(Long.toString(now.getTime()));
    }
    return codeString.toString();
  }
}
