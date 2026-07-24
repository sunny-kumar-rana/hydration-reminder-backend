package com.hydration.service.implementations;

import com.hydration.entity.User;
import com.hydration.entity.WaterIntake;
import com.hydration.repository.UserRepository;
import com.hydration.repository.WaterIntakeRepository;
import com.hydration.service.interfaces.EmailService;
import com.hydration.service.interfaces.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final UserRepository userRepository;
    private final WaterIntakeRepository waterIntakeRepository;
    private final EmailService emailService;

    @Override
    public void sendHydrationReminders() {

        List<User> users = userRepository.findAllByEmailNotificationEnabledTrue();

        for (User user : users) {

            if (Boolean.FALSE.equals(user.getEmailNotificationEnabled())) {
                continue;
            }

            LocalDate today = LocalDate.now();

            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.plusDays(1).atStartOfDay();

            List<WaterIntake> todayEntries =
                    waterIntakeRepository.findAllByUserAndConsumedAtBetween(
                            user,
                            start,
                            end
                    );

            int consumed = todayEntries.stream()
                    .mapToInt(WaterIntake::getAmount)
                    .sum();

            int goal = user.getDailyGoal() == null
                    ? 0
                    : user.getDailyGoal();

            if (goal <= 0) {
                continue;
            }

            if (consumed >= goal) {
                continue;
            }

            emailService.sendHydrationReminder(
                    user.getEmail(),
                    user.getUsername()
            );
        }
    }
}