package com.ebank.chatbot.kafka;

import com.ebank.chatbot.client.AccountClient;
import com.ebank.chatbot.llm.LlmReplyService;
import com.ebank.chatbot.model.AccountSummary;
import com.ebank.chatbot.telegram.TelegramNotifier;
import com.ebank.common.events.BankingTopics;
import com.ebank.common.events.TransactionCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventListener {
    private final AccountClient accountClient;
    private final LlmReplyService llmReplyService;
    private final TelegramNotifier telegramNotifier;

    public TransactionEventListener(AccountClient accountClient, LlmReplyService llmReplyService, TelegramNotifier telegramNotifier) {
        this.accountClient = accountClient;
        this.llmReplyService = llmReplyService;
        this.telegramNotifier = telegramNotifier;
    }

    @KafkaListener(topics = BankingTopics.TRANSACTIONS_CREATED, groupId = "chatbot-service")
    public void onTransaction(TransactionCreatedEvent event) {
        AccountSummary summary;
        try {
            summary = accountClient.getSummary(event.accountId());
        } catch (Exception ex) {
            summary = new AccountSummary(event.accountId(), event.amount(), event.currency());
        }

        String fallback = "Transaction %s of %s %s on account %s. Current balance: %s %s."
                .formatted(event.transactionId(), event.amount(), event.currency(), summary.accountId(), summary.currentBalance(), summary.currency());

        String prompt = "Create a concise Telegram banking notification for: " + fallback;
        String finalMessage = llmReplyService.buildReply(prompt, fallback);
        telegramNotifier.sendMessage(finalMessage);
    }
}
