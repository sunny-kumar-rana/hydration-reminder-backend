package com.hydration.controller;

import com.hydration.service.interfaces.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TelegramTestController {

    private final TelegramService telegramService;

    @GetMapping("/telegram")
    public String test() {

        telegramService.sendMessage(
                5301301737L,
                "✅ Telegram integration is working!"
        );

        return "Telegram message sent.";

    }

}