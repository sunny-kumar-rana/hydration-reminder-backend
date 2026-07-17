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
public class DailySummaryResponse {

    private LocalDate date;

    private Integer goal;

    private Integer consumed;

    private Integer remaining;

    private Double progressPercentage;
}