package com.devteam.module.http.get;

import com.devteam.module.common.ClientInfo;
import com.devteam.util.error.ErrorType;
import com.devteam.util.error.RuntimeError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GETService {
  private  Map<String, GETHandler> handlers = new HashMap<>();

  public void register(GETHandler handler) {
    if(handlers.containsKey(handler.getName())) {
      throw new RuntimeError(ErrorType.IllegalArgument, "Handler " + handler.getName() + " is already registered");
    }
    handlers.put(handler.getName(), handler);
  }

  @Autowired(required = false)
  public void autoRegister(List<GETHandler> handlers) {
    for(GETHandler handler : handlers) {
      register(handler);
    }
  }

  public GETContent get(ClientInfo client, String handler, String path) {
    return handlers.get(handler).get(client, path);
  }

  public GETContent get(String handler, String path) {
    return handlers.get(handler).get(path);
  }
}
