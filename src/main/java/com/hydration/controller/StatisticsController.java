package com.hydration.controller;

import com.hydration.dto.response.MonthlyStatisticsResponse;
import com.hydration.dto.response.StatisticsResponse;
import com.hydration.dto.response.WeeklyStatisticsResponse;
import com.hydration.service.interfaces.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatistics() {
        return ResponseEntity.ok(
                statisticsService.getStatistics()
        );
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<WeeklyStatisticsResponse>> getWeeklyStatistics() {
        return ResponseEntity.ok(
                statisticsService.getWeeklyStatistics()
        );
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyStatisticsResponse>> getMonthlyStatistics() {
        return ResponseEntity.ok(
                statisticsService.getMonthlyStatistics()
        );
    }
}