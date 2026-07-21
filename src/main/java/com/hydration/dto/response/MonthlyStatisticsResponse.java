package com.hydration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatisticsResponse {

    private YearMonth month;

    private Integer totalWater;

    private Double averagePerDay;

    private Integer goalAchievedDays;
}