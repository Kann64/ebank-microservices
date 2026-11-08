package com.ebank.account.model;

import java.math.BigDecimal;

public record AccountSummary(String accountId, BigDecimal currentBalance, String currency) {
}
