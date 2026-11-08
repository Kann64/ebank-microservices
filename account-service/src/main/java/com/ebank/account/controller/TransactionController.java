package com.ebank.account.controller;

import com.ebank.account.dto.CreateTransactionRequest;
import com.ebank.account.model.AccountSummary;
import com.ebank.account.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<Map<String, String>> createTransaction(
            @PathVariable("accountId") String accountId,
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        String txId = transactionService.createTransaction(accountId, request);
        return ResponseEntity.accepted().body(Map.of("transactionId", txId, "status", "published"));
    }

    @GetMapping("/{accountId}/summary")
    public AccountSummary summary(@PathVariable("accountId") String accountId) {
        return transactionService.getSummary(accountId);
    }
}
