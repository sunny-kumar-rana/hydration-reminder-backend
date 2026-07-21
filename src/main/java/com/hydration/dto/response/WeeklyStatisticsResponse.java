package com.hydration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyStatisticsResponse {

    private LocalDate weekStart;

    private LocalDate weekEnd;

    private Integer totalWater;

    private Double averagePerDay;
}