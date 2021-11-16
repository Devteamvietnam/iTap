package com.devteam.core.module.data.batch;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;


public class ListItemReader<T> implements ItemReader<T> {
  private List<T> items;
  private int currentIdx = 0;
  
  public ListItemReader(List<T> items) {
    this.items = items;
    this.currentIdx = 0;
  }
  
  @Override
  public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if(currentIdx >= items.size()) return null;
    T item = items.get(currentIdx++);
    return item;
  }
}
