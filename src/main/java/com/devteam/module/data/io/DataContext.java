package com.devteam.module.data.io;

import java.util.HashMap;
import java.util.Map;

public class DataContext {
  
  public static enum ATTRIBUTE { IS_IMPORT };
  
  private Map<String, Object> map = new HashMap<>();
  
  public <T> T getAttribute(String name) {
    return (T) map.get(name);
  }

  public <T> T getAttribute(Class<T> clazz) {
    return (T) map.get(clazz.getName());
  }

  public <T> void addAttribute(T object) {
    map.put(object.getClass().getName(), object);
  }
  
  public <T> void addAttribute(String name, T object) {
    map.put(name, object);
  }
}
