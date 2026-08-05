package com.motorny.service.impl;

import com.motorny.service.TelegramNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
public class TelegramNotificationServiceImpl implements TelegramNotificationService {

    private final String botToken;
    private final RestClient restClient;

    public TelegramNotificationServiceImpl(@Value("${telegram.bot.token}") String botToken) {
        this.botToken = botToken;
        this.restClient = RestClient.create();
    }

    @Override
    public void sendMessage(String chatId, String message) {
        if (chatId == null || chatId.isBlank()) {
            log.warn("TelegramNotification: chatId is empty, skipping notification");
            return;
        }

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        try {
            restClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .body(Map.of(
                            "chat_id", chatId,
                            "text", message,
                            "parse_mode", "HTML"
                    ))
                    .retrieve()
                    .toBodilessEntity();

            log.info("TelegramNotification: message sent to chatId={}", chatId);
        } catch (Exception e) {
            log.error("TelegramNotification: failed to send message to chatId={}: {}", chatId, e.getMessage());
        }
    }

    @Override
    public void notifyOrderAccepted(String telegramChatId, Long trackingNumber, String courierName) {
        String message = """
                ✅ <b>Your order has been accepted!</b>
                
                📦 Tracking number: <b>%d</b>
                🚚 Courier: <b>%s</b>
                
                You can track the delivery on the map in your personal cabinet.
                """.formatted(trackingNumber, courierName);

        sendMessage(telegramChatId, message);
    }
}
