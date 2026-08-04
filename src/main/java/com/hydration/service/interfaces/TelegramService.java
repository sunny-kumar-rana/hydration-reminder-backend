package com.hydration.service.interfaces;

public interface TelegramService {

    void sendHydrationReminder(String chatId, String username, Integer consumed, Integer goal);

    void sendGoalAchieved(String chatId, String username, Integer goal);

    void sendTestMessage(String chatId, String username);

}