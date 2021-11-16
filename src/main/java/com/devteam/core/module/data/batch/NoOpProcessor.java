package com.devteam.core.module.data.batch;

import org.springframework.batch.item.ItemProcessor;

public class NoOpProcessor<V, R> implements ItemProcessor<V, R> {
  @Override
  public R process(V item) throws Exception {
    return null;
  }
  
}