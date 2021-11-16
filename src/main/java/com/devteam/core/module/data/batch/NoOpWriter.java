package com.devteam.core.module.data.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class NoOpWriter<T> implements ItemWriter<T> {

  @Override
  public void write(List<? extends T> reader) throws Exception {
    System.out.println("Write sheet reader: ");
  }
}
