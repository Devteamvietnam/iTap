package com.devteam.data.activity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
@ComponentScan(basePackages = {
        "com.devteam.data.activity"
})
@EnableJpaRepositories(
  basePackages  = {
          "com.devteam.data.activity.repository",
  }
)
public class TransactionActivityConfiguration {
  final static public String CHANNEL_NAME = "transaction-activity-channel";

  @Bean(CHANNEL_NAME)
  public MessageChannel transactionActivityChannel() {
    return new DirectChannel();
  }

  @Bean("transactionActivitySource")
  @InboundChannelAdapter(channel = CHANNEL_NAME, poller = @Poller(fixedRate = "1000", maxMessagesPerPoll="10"))
  public TransactionActivitySource transactionActivitySource() {
    TransactionActivitySource sourceReader= new TransactionActivitySource();
    return sourceReader;
  }

  @Bean("transactionActivityHandler")
  @ServiceActivator(inputChannel= CHANNEL_NAME)
  public TransactionActivityMessageHandler transactionActivityHandler() {
    TransactionActivityMessageHandler handler = new TransactionActivityMessageHandler();
    return handler;
  }
}