package com.hydration.service.interfaces;

import com.hydration.dto.response.DashboardResponse;
import com.hydration.dto.response.MonthlyProgressResponse;
import com.hydration.dto.response.StreakResponse;
import com.hydration.dto.response.WeeklyProgressResponse;

import java.util.List;

public interface DashboardService {

    DashboardResponse getDashboard();

    List<WeeklyProgressResponse> getWeeklyProgress();

    List<MonthlyProgressResponse> getMonthlyProgress();

    StreakResponse getStreak();

}