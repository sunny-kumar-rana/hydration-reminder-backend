package com.hydration.service.implementations;

import com.hydration.dto.response.MonthlyStatisticsResponse;
import com.hydration.dto.response.StatisticsResponse;
import com.hydration.dto.response.WeeklyStatisticsResponse;
import com.hydration.entity.User;
import com.hydration.entity.WaterIntake;
import com.hydration.repository.WaterIntakeRepository;
import com.hydration.service.AuthenticatedUserService;
import com.hydration.service.interfaces.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final WaterIntakeRepository waterIntakeRepository;
    private final AuthenticatedUserService authenticatedUserService;

    @Override
    public StatisticsResponse getStatistics() {

        User user = authenticatedUserService.getCurrentUser();

        List<WaterIntake> entries =
                waterIntakeRepository.findAllByUserOrderByConsumedAtAsc(user);

        if (entries.isEmpty()) {
            return new StatisticsResponse(
                    0,
                    0,
                    0.0,
                    0.0,
                    0,
                    0,
                    0,
                    0
            );
        }

        int totalWater = entries.stream()
                .mapToInt(WaterIntake::getAmount)
                .sum();

        int totalEntries = entries.size();

        double averagePerEntry =
                (double) totalWater / totalEntries;

        Map<LocalDate, Integer> dailyTotals = new TreeMap<>();

        for (WaterIntake intake : entries) {

            LocalDate date = intake.getConsumedAt().toLocalDate();

            dailyTotals.merge(
                    date,
                    intake.getAmount(),
                    Integer::sum
            );
        }

        double averagePerDay =
                (double) totalWater / dailyTotals.size();

        int highestSingleIntake = entries.stream()
                .mapToInt(WaterIntake::getAmount)
                .max()
                .orElse(0);

        int goal = user.getDailyGoal() == null
                ? 0
                : user.getDailyGoal();

        int goalAchievedDays = 0;
        int currentStreak = 0;
        int longestStreak = 0;
        int runningStreak = 0;

        if (goal > 0) {

            LocalDate firstDay = dailyTotals.keySet()
                    .stream()
                    .min(LocalDate::compareTo)
                    .orElse(LocalDate.now());

            LocalDate today = LocalDate.now();

            // Longest streak & achieved days
            for (LocalDate date = firstDay;
                 !date.isAfter(today);
                 date = date.plusDays(1)) {

                int consumed = dailyTotals.getOrDefault(date, 0);

                if (consumed >= goal) {

                    goalAchievedDays++;
                    runningStreak++;

                    longestStreak = Math.max(
                            longestStreak,
                            runningStreak
                    );

                } else {

                    runningStreak = 0;
                }
            }

            // Current streak
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
        }

        return new StatisticsResponse(
                totalWater,
                totalEntries,
                averagePerEntry,
                averagePerDay,
                highestSingleIntake,
                goalAchievedDays,
                currentStreak,
                longestStreak
        );
    }

    @Override
    public List<WeeklyStatisticsResponse> getWeeklyStatistics() {

        User user = authenticatedUserService.getCurrentUser();

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

        List<WeeklyStatisticsResponse> response = new ArrayList<>();

        LocalDate today = LocalDate.now();

        LocalDate weekStart = today.minusWeeks(3);

        weekStart = weekStart.with(java.time.DayOfWeek.MONDAY);

        while (!weekStart.isAfter(today)) {

            LocalDate weekEnd = weekStart.plusDays(6);

            int totalWater = 0;
            int days = 0;

            for (LocalDate date = weekStart;
                 !date.isAfter(weekEnd) && !date.isAfter(today);
                 date = date.plusDays(1)) {

                totalWater += dailyTotals.getOrDefault(date, 0);
                days++;
            }

            double averagePerDay =
                    days == 0
                            ? 0
                            : (double) totalWater / days;

            response.add(
                    new WeeklyStatisticsResponse(
                            weekStart,
                            weekEnd,
                            totalWater,
                            averagePerDay
                    )
            );

            weekStart = weekStart.plusWeeks(1);
        }

        return response;
    }

    @Override
    public List<MonthlyStatisticsResponse> getMonthlyStatistics() {

        User user = authenticatedUserService.getCurrentUser();

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

        int goal = user.getDailyGoal() == null
                ? 0
                : user.getDailyGoal();

        List<MonthlyStatisticsResponse> response = new ArrayList<>();

        YearMonth currentMonth = YearMonth.now();

        YearMonth startMonth = currentMonth.minusMonths(5);

        while (!startMonth.isAfter(currentMonth)) {

            LocalDate monthStart = startMonth.atDay(1);
            LocalDate monthEnd = startMonth.atEndOfMonth();

            int totalWater = 0;
            int goalAchievedDays = 0;

            for (LocalDate date = monthStart;
                 !date.isAfter(monthEnd);
                 date = date.plusDays(1)) {

                int consumed = dailyTotals.getOrDefault(date, 0);

                totalWater += consumed;

                if (goal > 0 && consumed >= goal) {
                    goalAchievedDays++;
                }
            }

            double averagePerDay =
                    (double) totalWater / monthEnd.lengthOfMonth();

            response.add(
                    new MonthlyStatisticsResponse(
                            startMonth,
                            totalWater,
                            averagePerDay,
                            goalAchievedDays
                    )
            );

            startMonth = startMonth.plusMonths(1);
        }

        return response;
    }
}