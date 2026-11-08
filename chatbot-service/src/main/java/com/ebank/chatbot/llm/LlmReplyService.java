package com.ebank.chatbot.llm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LlmReplyService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${llm.api-url:}")
    private String llmApiUrl;

    @Value("${llm.api-key:}")
    private String llmApiKey;

    @Value("${llm.model:gpt-4o-mini}")
    private String model;

    public String buildReply(String prompt, String fallbackMessage) {
        if (llmApiUrl == null || llmApiUrl.isBlank() || llmApiKey == null || llmApiKey.isBlank()) {
            return fallbackMessage;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(llmApiKey);

        Map<String, Object> body = Map.of(
                "model", model,
                "input", prompt
        );

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(llmApiUrl, new HttpEntity<>(body, headers), String.class);
            String raw = response.getBody();
            return (raw == null || raw.isBlank()) ? fallbackMessage : raw;
        } catch (Exception ignored) {
            return fallbackMessage;
        }
    }
}
