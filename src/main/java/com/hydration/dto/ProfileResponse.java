package com.hydration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private String username;
    private String email;
    private Integer dailyGoal;
    private String telegramChatId;
    private Boolean emailNotificationEnabled;
    private Boolean telegramNotificationEnabled;
    private String timezone;
    private LocalDateTime createdAt;
}
