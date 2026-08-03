package com.hydration.service.implementations;

import com.hydration.service.interfaces.TelegramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Override
    public void sendMessage(Long chatId, String message) {

        String url = apiUrl +
                "/bot" +
                botToken +
                "/sendMessage?chat_id=" +
                chatId +
                "&text=" +
                message;

        restTemplate.getForObject(url, String.class);

    }
}