package com.motorny.service;

public interface TelegramNotificationService {

    /**
     * Sends a text message to the specified Telegram chat.
     *
     * @param chatId  the recipient's Telegram chat ID
     * @param message the text to send
     */
    void sendMessage(String chatId, String message);

    /**
     * Notifies the client that their order has been accepted by a courier.
     *
     * @param telegramChatId client's Telegram chat ID
     * @param trackingNumber shipment tracking number
     * @param courierName    full name of the assigned courier
     */
    void notifyOrderAccepted(String telegramChatId, Long trackingNumber, String courierName);
}
