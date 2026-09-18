package com.hydration.controller;

import com.hydration.dto.response.MonthlyStatisticsResponse;
import com.hydration.dto.response.StatisticsResponse;
import com.hydration.dto.response.WeeklyStatisticsResponse;
import com.hydration.security.JwtAuthenticationFilter;
import com.hydration.security.JwtService;
import com.hydration.service.interfaces.StatisticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatisticsController.class)
@AutoConfigureMockMvc(addFilters = false)
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatisticsService statisticsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getStatistics_shouldReturnOk() throws Exception {

        StatisticsResponse response =
                new StatisticsResponse(
                        15000,
                        10,
                        1500.0,
                        2142.86,
                        1000,
                        5,
                        3,
                        4
                );

        when(statisticsService.getStatistics())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/statistics")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalWaterConsumed")
                        .value(15000))
                .andExpect(jsonPath("$.totalEntries")
                        .value(10))
                .andExpect(jsonPath("$.averagePerEntry")
                        .value(1500.0))
                .andExpect(jsonPath("$.averagePerDay")
                        .value(2142.86))
                .andExpect(jsonPath("$.highestSingleIntake")
                        .value(1000))
                .andExpect(jsonPath("$.goalAchievedDays")
                        .value(5))
                .andExpect(jsonPath("$.currentStreak")
                        .value(3))
                .andExpect(jsonPath("$.longestStreak")
                        .value(4));

        verify(statisticsService).getStatistics();
    }

    @Test
    void getWeeklyStatistics_shouldReturnOk() throws Exception {

        List<WeeklyStatisticsResponse> response = List.of(
                new WeeklyStatisticsResponse(
                        LocalDate.of(2026, 9, 14),
                        LocalDate.of(2026, 9, 20),
                        12000,
                        1714.29
                )
        );

        when(statisticsService.getWeeklyStatistics())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/statistics/weekly")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].weekStart")
                        .value("2026-09-14"))
                .andExpect(jsonPath("$[0].weekEnd")
                        .value("2026-09-20"))
                .andExpect(jsonPath("$[0].totalWater")
                        .value(12000))
                .andExpect(jsonPath("$[0].averagePerDay")
                        .value(1714.29));

        verify(statisticsService).getWeeklyStatistics();
    }

    @Test
    void getMonthlyStatistics_shouldReturnOk() throws Exception {

        List<MonthlyStatisticsResponse> response = List.of(
                new MonthlyStatisticsResponse(
                        YearMonth.of(2026, 9),
                        45000,
                        1500.0,
                        15
                )
        );

        when(statisticsService.getMonthlyStatistics())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/statistics/monthly")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].month")
                        .value("2026-09"))
                .andExpect(jsonPath("$[0].totalWater")
                        .value(45000))
                .andExpect(jsonPath("$[0].averagePerDay")
                        .value(1500.0))
                .andExpect(jsonPath("$[0].goalAchievedDays")
                        .value(15));

        verify(statisticsService).getMonthlyStatistics();
    }
}