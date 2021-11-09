package com.devteam.data.activity;

import com.devteam.data.activity.entity.TransactionActivity;
import com.devteam.util.error.ErrorType;
import com.devteam.util.error.RuntimeError;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TransactionActivitySource implements MessageSource<TransactionActivity> {
  private BlockingQueue<TransactionActivity> queue = new LinkedBlockingQueue<>(500);

  public void enqueue(TransactionActivity tActivity) {
    try {
      queue.offer(tActivity, 1000, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeError(ErrorType.IllegalState, "Queue is full!!!");
    }
  }

  @Override
  public Message<TransactionActivity> receive() {
    try {
      TransactionActivity tActivity = queue.poll(1, TimeUnit.MILLISECONDS);
      if(tActivity == null) return null;
      return new GenericMessage<TransactionActivity>(tActivity);
    } catch (InterruptedException e) {
      return null;
    }
  }
}