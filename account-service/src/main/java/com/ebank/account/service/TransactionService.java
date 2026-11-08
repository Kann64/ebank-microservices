package com.ebank.account.service;

import com.ebank.account.dto.CreateTransactionRequest;
import com.ebank.account.messaging.TransactionEventPublisher;
import com.ebank.account.model.AccountSummary;
import com.ebank.common.events.TransactionCreatedEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TransactionService {
    private final TransactionEventPublisher publisher;
    private final ConcurrentHashMap<String, BigDecimal> balances = new ConcurrentHashMap<>();

    public TransactionService(TransactionEventPublisher publisher) {
        this.publisher = publisher;
    }

    public String createTransaction(String accountId, CreateTransactionRequest request) {
        String transactionId = UUID.randomUUID().toString();
        balances.merge(accountId, request.amount(), BigDecimal::add);

        TransactionCreatedEvent event = new TransactionCreatedEvent(
                transactionId,
                accountId,
                request.amount(),
                request.currency(),
                request.description(),
                Instant.now()
        );
        publisher.publish(event);
        return transactionId;
    }

    public AccountSummary getSummary(String accountId) {
        BigDecimal balance = balances.getOrDefault(accountId, BigDecimal.ZERO);
        return new AccountSummary(accountId, balance, "USD");
    }
}
