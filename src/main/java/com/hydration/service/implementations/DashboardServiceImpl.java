package com.hydration.service.implementations;

import com.hydration.dto.response.DashboardResponse;
import com.hydration.dto.response.MonthlyProgressResponse;
import com.hydration.dto.response.StreakResponse;
import com.hydration.dto.response.WeeklyProgressResponse;
import com.hydration.entity.User;
import com.hydration.entity.WaterIntake;
import com.hydration.repository.WaterIntakeRepository;
import com.hydration.service.AuthenticatedUserService;
import com.hydration.service.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
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
                streak.getLongestStreak(),
                user.getUsername()
        );
    }

    @Override
    public List<WeeklyProgressResponse> getWeeklyProgress() {

        User user = authenticatedUserService.getCurrentUser();

        int goal = user.getDailyGoal() == null
                ? 0
                : user.getDailyGoal();

        Map<LocalDate, Integer> dailyTotals = getDailyTotals(user);

        List<WeeklyProgressResponse> response = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {

            LocalDate date = LocalDate.now().minusDays(i);

            int consumed = dailyTotals.getOrDefault(date, 0);

            double progress = goal == 0
                    ? 0
                    : (consumed * 100.0) / goal;

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

        Map<LocalDate, Integer> dailyTotals = getDailyTotals(user);

        List<MonthlyProgressResponse> response = new ArrayList<>();

        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);

        LocalDate lastDay = LocalDate.now();

        for (LocalDate date = firstDay;
             !date.isAfter(lastDay);
             date = date.plusDays(1)) {

            int consumed = dailyTotals.getOrDefault(date, 0);

            double progress = goal == 0
                    ? 0
                    : (consumed * 100.0) / goal;

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

        Map<LocalDate, Integer> dailyTotals = getDailyTotals(user);

        if (dailyTotals.isEmpty()) {
            return new StreakResponse(0, 0, 0);
        }

        LocalDate firstDay = dailyTotals.keySet()
                .stream()
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());

        LocalDate today = LocalDate.now();

        int currentStreak = 0;
        int longestStreak = 0;
        int runningStreak = 0;
        int achievedDays = 0;

        for (LocalDate date = firstDay;
             !date.isAfter(today);
             date = date.plusDays(1)) {

            int consumed = dailyTotals.getOrDefault(date, 0);

            if (consumed >= goal) {

                achievedDays++;
                runningStreak++;

                longestStreak = Math.max(
                        longestStreak,
                        runningStreak
                );

            } else {

                runningStreak = 0;
            }
        }

        for (LocalDate date = today;
             !date.isBefore(firstDay);
             date = date.minusDays(1)) {

            int consumed = dailyTotals.getOrDefault(date, 0);

            if (consumed >= goal) {
                currentStreak++;
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

    private Map<LocalDate, Integer> getDailyTotals(User user) {

        List<WaterIntake> entries =
                waterIntakeRepository.findAllByUserOrderByConsumedAtAsc(user);

        Map<LocalDate, Integer> dailyTotals = new TreeMap<>();

        for (WaterIntake intake : entries) {

            dailyTotals.merge(
                    intake.getConsumedAt().toLocalDate(),
                    intake.getAmount(),
                    Integer::sum
            );
        }

        return dailyTotals;
    }
}