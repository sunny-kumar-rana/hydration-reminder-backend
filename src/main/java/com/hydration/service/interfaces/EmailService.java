package com.hydration.service.interfaces;

public interface EmailService {

    void sendHydrationReminder(String to, String username);

    void sendGoalAchievedEmail(String to, String username);

}