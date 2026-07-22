package com.hydration.service.implementations;

import com.hydration.dto.response.MonthlyStatisticsResponse;
import com.hydration.dto.response.StatisticsResponse;
import com.hydration.dto.response.WeeklyStatisticsResponse;
import com.hydration.repository.WaterIntakeRepository;
import com.hydration.service.AuthenticatedUserService;
import com.hydration.service.interfaces.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final WaterIntakeRepository waterIntakeRepository;
    private final AuthenticatedUserService authenticatedUserService;

    @Override
    public StatisticsResponse getStatistics() {
        return null;
    }

    @Override
    public List<WeeklyStatisticsResponse> getWeeklyStatistics() {
        return null;
    }

    @Override
    public List<MonthlyStatisticsResponse> getMonthlyStatistics() {
        return null;
    }
}