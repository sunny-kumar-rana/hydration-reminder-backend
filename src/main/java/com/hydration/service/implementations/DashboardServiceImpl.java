package com.hydration.service.implementations;

import com.hydration.dto.response.*;
import com.hydration.entity.User;
import com.hydration.entity.WaterIntake;
import com.hydration.repository.UserRepository;
import com.hydration.repository.WaterIntakeRepository;
import com.hydration.service.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final WaterIntakeRepository waterIntakeRepository;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    private User getCurrentUser() {
        return userRepository.findByUsername(getCurrentUsername())
                .orElseThrow();
    }

    @Override
    public DashboardResponse getDashboard() {

        User user = getCurrentUser();

        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        List<WaterIntake> todayEntries =
                waterIntakeRepository.findAllByUserAndConsumedAtBetween(
                        user,
                        start,
                        end
                );

        int todayConsumed = todayEntries.stream()
                .mapToInt(WaterIntake::getAmount)
                .sum();

        int goal = user.getDailyGoal() == null
                ? 0
                : user.getDailyGoal();

        int remaining = Math.max(goal - todayConsumed, 0);

        double progress = goal == 0
                ? 0
                : (todayConsumed * 100.0) / goal;

        int todayEntryCount = todayEntries.size();

        StreakResponse streak = getStreak();

        return new DashboardResponse(
                goal,
                todayConsumed,
                remaining,
                progress,
                todayEntryCount,
                streak.getCurrentStreak(),
                streak.getLongestStreak()
        );
    }

    @Override
    public List<WeeklyProgressResponse> getWeeklyProgress() {

        User user = getCurrentUser();

        int goal = user.getDailyGoal() == null
                ? 0
                : user.getDailyGoal();

        List<WeeklyProgressResponse> response = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {

            LocalDate date = LocalDate.now().minusDays(i);

            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            int consumed = waterIntakeRepository
                    .findAllByUserAndConsumedAtBetween(user, start, end)
                    .stream()
                    .mapToInt(WaterIntake::getAmount)
                    .sum();

            double progress = goal == 0
                    ? 0
                    : consumed * 100.0 / goal;

            response.add(
                    new WeeklyProgressResponse(
                            date,
                            consumed,
                            goal,
                            progress
                    )
            );
        }

        return response;
    }

    @Override
    public List<MonthlyProgressResponse> getMonthlyProgress() {

        User user = getCurrentUser();

        int goal = user.getDailyGoal() == null
                ? 0
                : user.getDailyGoal();

        List<MonthlyProgressResponse> response = new ArrayList<>();

        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDay = LocalDate.now();

        for (LocalDate date = firstDay;
             !date.isAfter(lastDay);
             date = date.plusDays(1)) {

            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            int consumed = waterIntakeRepository
                    .findAllByUserAndConsumedAtBetween(user, start, end)
                    .stream()
                    .mapToInt(WaterIntake::getAmount)
                    .sum();

            double progress = goal == 0
                    ? 0
                    : consumed * 100.0 / goal;

            response.add(
                    new MonthlyProgressResponse(
                            date,
                            consumed,
                            goal,
                            progress
                    )
            );
        }

        return response;
    }

    @Override
    public StreakResponse getStreak() {
        return null;
    }
}