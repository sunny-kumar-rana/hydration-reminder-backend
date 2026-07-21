package com.hydration.controller;

import com.hydration.dto.response.DashboardResponse;
import com.hydration.dto.response.MonthlyProgressResponse;
import com.hydration.dto.response.StreakResponse;
import com.hydration.dto.response.WeeklyProgressResponse;
import com.hydration.service.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(
                dashboardService.getDashboard()
        );
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<WeeklyProgressResponse>> getWeeklyProgress() {
        return ResponseEntity.ok(
                dashboardService.getWeeklyProgress()
        );
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyProgressResponse>> getMonthlyProgress() {
        return ResponseEntity.ok(
                dashboardService.getMonthlyProgress()
        );
    }

    @GetMapping("/streak")
    public ResponseEntity<StreakResponse> getStreak() {
        return ResponseEntity.ok(
                dashboardService.getStreak()
        );
    }
}