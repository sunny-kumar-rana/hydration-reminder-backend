package com.hydration.controller;

import com.hydration.dto.response.DashboardResponse;
import com.hydration.dto.response.MonthlyProgressResponse;
import com.hydration.dto.response.StreakResponse;
import com.hydration.dto.response.WeeklyProgressResponse;
import com.hydration.security.JwtAuthenticationFilter;
import com.hydration.security.JwtService;
import com.hydration.service.interfaces.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardService dashboardService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getDashboard_shouldReturnOk() throws Exception {

        DashboardResponse response =
                new DashboardResponse(
                        2500,
                        1500,
                        1000,
                        60.0,
                        3,
                        2,
                        5,
                        "john123"
                );

        when(dashboardService.getDashboard())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/dashboard")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dailyGoal").value(2500))
                .andExpect(jsonPath("$.todayConsumed").value(1500))
                .andExpect(jsonPath("$.remaining").value(1000))
                .andExpect(jsonPath("$.progressPercentage").value(60.0))
                .andExpect(jsonPath("$.todayEntries").value(3))
                .andExpect(jsonPath("$.currentStreak").value(2))
                .andExpect(jsonPath("$.longestStreak").value(5))
                .andExpect(jsonPath("$.username").value("john123"));

        verify(dashboardService).getDashboard();
    }

    @Test
    void getWeeklyProgress_shouldReturnOk() throws Exception {

        List<WeeklyProgressResponse> response = List.of(
                new WeeklyProgressResponse(
                        LocalDate.of(2026, 9, 12),
                        2000,
                        2500,
                        80.0
                ),
                new WeeklyProgressResponse(
                        LocalDate.of(2026, 9, 13),
                        2500,
                        2500,
                        100.0
                )
        );

        when(dashboardService.getWeeklyProgress())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/dashboard/weekly")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].date")
                        .value("2026-09-12"))
                .andExpect(jsonPath("$[0].consumed").value(2000))
                .andExpect(jsonPath("$[0].goal").value(2500))
                .andExpect(jsonPath("$[0].progressPercentage")
                        .value(80.0));

        verify(dashboardService).getWeeklyProgress();
    }

    @Test
    void getMonthlyProgress_shouldReturnOk() throws Exception {

        List<MonthlyProgressResponse> response = List.of(
                new MonthlyProgressResponse(
                        LocalDate.of(2026, 9, 1),
                        1800,
                        2500,
                        72.0
                )
        );

        when(dashboardService.getMonthlyProgress())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/dashboard/monthly")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].date")
                        .value("2026-09-01"))
                .andExpect(jsonPath("$[0].consumed").value(1800))
                .andExpect(jsonPath("$[0].goal").value(2500))
                .andExpect(jsonPath("$[0].progressPercentage")
                        .value(72.0));

        verify(dashboardService).getMonthlyProgress();
    }

    @Test
    void getStreak_shouldReturnOk() throws Exception {

        StreakResponse response =
                new StreakResponse(
                        3,
                        7,
                        10
                );

        when(dashboardService.getStreak())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/dashboard/streak")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentStreak").value(3))
                .andExpect(jsonPath("$.longestStreak").value(7))
                .andExpect(jsonPath("$.totalGoalAchievedDays")
                        .value(10));

        verify(dashboardService).getStreak();
    }
}