package com.devteam.data.activity;

import com.devteam.config.ClientInfo;
import com.devteam.data.activity.entity.TransactionActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class TransactionActivityMessageHandler implements MessageHandler {
  @Autowired
  private TransactionActivityService service;

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    TransactionActivity tActivity = (TransactionActivity) message.getPayload();
    service.saveTransactionActivity(ClientInfo.DEFAULT, tActivity);
  }
}