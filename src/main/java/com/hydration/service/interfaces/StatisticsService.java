package com.hydration.service.interfaces;

import com.hydration.dto.response.MonthlyStatisticsResponse;
import com.hydration.dto.response.StatisticsResponse;
import com.hydration.dto.response.WeeklyStatisticsResponse;

import java.util.List;

public interface StatisticsService {

    StatisticsResponse getStatistics();

    List<WeeklyStatisticsResponse> getWeeklyStatistics();

    List<MonthlyStatisticsResponse> getMonthlyStatistics();

}