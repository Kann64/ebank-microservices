package com.ebank.account.controller;

import com.ebank.account.dto.CreateTransactionRequest;
import com.ebank.account.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Test
    void createTransactionPublishesAndReturnsAccepted() throws Exception {
        when(transactionService.createTransaction(ArgumentMatchers.eq("acc-1"), ArgumentMatchers.any(CreateTransactionRequest.class)))
                .thenReturn("tx-1");

        CreateTransactionRequest request = new CreateTransactionRequest(new BigDecimal("10.25"), "USD", "Deposit");

        mockMvc.perform(post("/api/accounts/acc-1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.transactionId").value("tx-1"))
                .andExpect(jsonPath("$.status").value("published"));

        verify(transactionService).createTransaction(ArgumentMatchers.eq("acc-1"), ArgumentMatchers.any(CreateTransactionRequest.class));
    }
}
