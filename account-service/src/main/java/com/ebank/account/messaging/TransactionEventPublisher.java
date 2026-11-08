package com.ebank.account.messaging;

import com.ebank.common.events.BankingTopics;
import com.ebank.common.events.TransactionCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventPublisher {
    private final KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate;

    public TransactionEventPublisher(KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(TransactionCreatedEvent event) {
        kafkaTemplate.send(BankingTopics.TRANSACTIONS_CREATED, event.accountId(), event);
    }
}
