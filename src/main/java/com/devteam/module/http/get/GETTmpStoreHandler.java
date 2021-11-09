package com.devteam.module.http.get;

import com.devteam.config.CachingConfig;
import com.devteam.config.ClientInfo;
import com.devteam.util.cipher.MD5;
import com.devteam.util.ds.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class GETTmpStoreHandler extends GETHandler {
  private Cache storeCache;

  public GETTmpStoreHandler(
      @Autowired @Qualifier(CachingConfig.DISK_CACHE_MANAGER) CacheManager diskCacheManager) {
    super("store");
    storeCache = diskCacheManager.getCache("generic");
  }


  @Override
  public GETContent get(ClientInfo client, String path) {
    String storeId = path;
    ValueWrapper valueWrapper = storeCache.get(createKey(storeId));
    Objects.assertNotNull(valueWrapper, "The content for store " + storeId + " is not available");
    return (GETContent) valueWrapper.get();
  }

  public StoreInfo store(ClientInfo client, GETContent content) {
    String storeId = MD5.digest(client.getRemoteUser() + ":/" + content.getFileName()).toString();
    storeCache.put(createKey(storeId), content);
    return new StoreInfo(storeId, content);
  }

  String createKey(String storeId) {
    return "tmp:store:"  + storeId;
  }
}
