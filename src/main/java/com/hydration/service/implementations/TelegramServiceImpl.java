package com.hydration.service.implementations;

import com.hydration.service.interfaces.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class TelegramServiceImpl implements TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public TelegramServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private void sendMessage(String chatId, String message) {

        String url = apiUrl
                + "/bot"
                + botToken
                + "/sendMessage";

        restTemplate.getForObject(
                UriComponentsBuilder
                        .fromUriString(url)
                        .queryParam("chat_id", chatId)
                        .queryParam("text", message)
                        .build(false)
                        .toUri(),
                String.class
        );

        log.info(
                "Telegram message sent successfully to chatId '{}'.",
                chatId
        );
    }

    @Override
    public void sendHydrationReminder(
            String chatId,
            String username,
            Integer consumed,
            Integer goal
    ) {

        int remaining = goal - consumed;

        String message = """
            💧 Hydration Reminder

            Hi %s 👋

            Today's Intake
            🥤 %d ml

            Daily Goal
            🎯 %d ml

            Remaining
            💦 %d ml

            Stay hydrated!
            """
                .formatted(
                        username,
                        consumed,
                        goal,
                        remaining
                );

        sendMessage(chatId, message);

    }

    @Override
    public void sendGoalAchieved(
            String chatId,
            String username,
            Integer goal
    ) {

        String message = """
            🎉 Congratulations %s!

            You reached today's goal.

            🎯 %d ml

            Keep up the great work! 💙
            """
                .formatted(
                        username,
                        goal
                );

        sendMessage(chatId, message);

    }

    @Override
    public void sendTestMessage(
            String chatId,
            String username
    ) {

        String message = """
            ✅ Test Notification

            Hi %s 👋

            Telegram notifications are configured correctly.

            Future hydration reminders will be delivered here.

            💧 Hydration Tracker
            """
                .formatted(username);

        sendMessage(chatId, message);

    }
}