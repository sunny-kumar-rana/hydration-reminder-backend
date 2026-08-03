package com.hydration.service.interfaces;

public interface TelegramService {

    void sendMessage(Long chatId, String message);

}