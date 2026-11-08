package com.ebank.chatbot.client;

import com.ebank.chatbot.model.AccountSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("/api/accounts/{accountId}/summary")
    AccountSummary getSummary(@PathVariable("accountId") String accountId);
}
