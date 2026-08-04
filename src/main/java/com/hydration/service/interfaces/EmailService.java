package com.hydration.service.interfaces;

public interface EmailService {

    void sendHydrationReminder(String to, String username);

    void sendGoalAchieved(String to, String username);

    void sendTestEmail(String email, String username);

}