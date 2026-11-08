package com.ebank.common.events;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionCreatedEvent(
        String transactionId,
        String accountId,
        BigDecimal amount,
        String currency,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant createdAt
) {
}
