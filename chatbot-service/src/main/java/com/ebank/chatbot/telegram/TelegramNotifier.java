package com.ebank.chatbot.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class TelegramNotifier {
    private static final Logger log = LoggerFactory.getLogger(TelegramNotifier.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${telegram.bot-token:}")
    private String botToken;

    @Value("${telegram.chat-id:}")
    private String chatId;

    public void sendMessage(String message) {
        if (botToken == null || botToken.isBlank() || chatId == null || chatId.isBlank()) {
            log.info("Telegram not configured. Message: {}", message);
            return;
        }

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(
                Map.of("chat_id", chatId, "text", message),
                headers
        );

        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception ex) {
            log.warn("Failed to send Telegram message", ex);
        }
    }
}
