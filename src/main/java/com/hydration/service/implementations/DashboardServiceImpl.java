package com.hydration.service.implementations;

import com.hydration.dto.response.*;
import com.hydration.entity.User;
import com.hydration.entity.WaterIntake;
import com.hydration.repository.UserRepository;
import com.hydration.repository.WaterIntakeRepository;
import com.hydration.service.AuthenticatedUserService;
import com.hydration.service.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AuthenticatedUserService authenticatedUserService;
    private final WaterIntakeRepository waterIntakeRepository;

    @Override
    public DashboardResponse getDashboard() {

        User user = authenticatedUserService.getCurrentUser();

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

        User user = authenticatedUserService.getCurrentUser();

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

        User user = authenticatedUserService.getCurrentUser();

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

        User user = authenticatedUserService.getCurrentUser();

        int goal = user.getDailyGoal() == null
                ? 0
                : user.getDailyGoal();

        if (goal == 0) {
            return new StreakResponse(0, 0, 0);
        }

        List<WaterIntake> entries =
                waterIntakeRepository.findAllByUserOrderByConsumedAtAsc(user);

        Map<LocalDate, Integer> dailyTotals = new TreeMap<>();

        for (WaterIntake intake : entries) {

            LocalDate date = intake.getConsumedAt().toLocalDate();

            dailyTotals.merge(
                    date,
                    intake.getAmount(),
                    Integer::sum
            );
        }

        int currentStreak = 0;
        int longestStreak = 0;
        int runningStreak = 0;
        int achievedDays = 0;

        for (Integer total : dailyTotals.values()) {

            if (total >= goal) {
                runningStreak++;
                achievedDays++;
                longestStreak = Math.max(longestStreak, runningStreak);
            } else {
                runningStreak = 0;
            }
        }

        LocalDate date = LocalDate.now();

        while (true) {

            Integer total = dailyTotals.get(date);

            if (total != null && total >= goal) {
                currentStreak++;
                date = date.minusDays(1);
            } else {
                break;
            }
        }

        return new StreakResponse(
                currentStreak,
                longestStreak,
                achievedDays
        );
    }
}