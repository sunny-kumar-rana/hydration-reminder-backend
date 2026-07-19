package com.hydration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StreakResponse {

    private Integer currentStreak;

    private Integer longestStreak;

    private Integer totalGoalAchievedDays;
}