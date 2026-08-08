package com.hydration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Integer dailyGoal;

    private Integer todayConsumed;

    private Integer remaining;

    private Double progressPercentage;

    private Integer todayEntries;

    private Integer currentStreak;

    private Integer longestStreak;

    private String username;
}