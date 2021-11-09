package com.devteam.config;


public interface ServiceMethodCallback<T> {
  public void onPreMethod() ;
  public void onPostMethod() ;
}
