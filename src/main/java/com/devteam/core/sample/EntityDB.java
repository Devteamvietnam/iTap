package com.devteam.core.sample;

import com.devteam.core.JPAService;
import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;


public class EntityDB {
  final static public String DEFAULT = "devteam";

  static EntityDB instance = null ;

  private ApplicationContext    context;
  Map<String, Object>      dataMap    = new HashMap<>();
  Map<String, CommunityDataMap> allCommunityDataMap = new HashMap<>();

  private EntityDB(ApplicationContext context) {
    this.context = context;
  }

  public EntityManager getEntityManager() {
    return context.getBean(JPAService.class).getEntityManager();
  }

  public <T> T getData(Class<T> type) {
    T data = (T) dataMap.get(type.getName());
    if(data == null) {
      data = context.getAutowireCapableBeanFactory().createBean(type);
      dataMap.put(type.getName(), data);
    }
    return data ;
  }

  public <T> T getCommunityData(Class<T> type) {
    return getData(DEFAULT, type);
  }

  public <T> T getData(String community, Class<T> type) {
    CommunityDataMap communityDataMap = allCommunityDataMap.get(community);
    if(communityDataMap == null) {
      communityDataMap = new CommunityDataMap();
      allCommunityDataMap.put(community, communityDataMap);
    }
    return communityDataMap.getData(context, type) ;
  }

  static public void initDataDB(ApplicationContext context) {
    instance = new EntityDB(context);
  }

  static public void clearDataDB() {
    instance = null;
  }

  static public EntityDB getInstance() { return instance ; }

  static public class CommunityDataMap {
    Map<String, Object> dataMap = new HashMap<>();

    public <T> T getData(ApplicationContext context, Class<T> type) {
      T data = (T) dataMap.get(type.getName());
      if(data == null) {
        data = context.getAutowireCapableBeanFactory().createBean(type);
        dataMap.put(type.getName(), data);
      }
      return data ;
    }
  }
}
