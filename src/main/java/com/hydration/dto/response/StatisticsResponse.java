package com.hydration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {

    private Integer totalWaterConsumed;

    private Integer totalEntries;

    private Double averagePerEntry;

    private Double averagePerDay;

    private Integer highestSingleIntake;

    private Integer goalAchievedDays;

    private Integer currentStreak;

    private Integer longestStreak;
}