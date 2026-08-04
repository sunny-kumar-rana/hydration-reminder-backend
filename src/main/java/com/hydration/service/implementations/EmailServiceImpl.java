package com.hydration.service.implementations;

import com.hydration.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendHydrationReminder(String to, String username) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("💧 Hydration Reminder");

        message.setText(
                "Hello " + username + ",\n\n" +
                        "This is a reminder to drink some water.\n\n" +
                        "Stay hydrated and have a great day!\n\n" +
                        "- Hydration Reminder"
        );

        mailSender.send(message);
    }

    @Override
    public void sendGoalAchieved(String to, String username) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("🎉 Daily Goal Achieved");

        message.setText(
                "Congratulations " + username + "!\n\n" +
                        "You have successfully achieved your daily hydration goal today.\n\n" +
                        "Keep up the great work!\n\n" +
                        "- Hydration Reminder"
        );

        mailSender.send(message);
    }

    @Override
    public void sendTestEmail(
            String email,
            String username
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);

        message.setSubject("Hydration Tracker - Test Notification");

        message.setText("""
            Hi %s,

            Your email notifications are working correctly.

            You will receive hydration reminders at this email address.

            💧 Happy Hydrating!
            """
                .formatted(username));

        mailSender.send(message);

    }
}