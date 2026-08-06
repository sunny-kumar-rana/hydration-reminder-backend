package com.hydration.service.implementations;

import com.hydration.entity.User;
import com.hydration.entity.WaterIntake;
import com.hydration.repository.UserRepository;
import com.hydration.repository.WaterIntakeRepository;
import com.hydration.service.interfaces.EmailService;
import com.hydration.service.interfaces.ReminderService;
import com.hydration.service.interfaces.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final UserRepository userRepository;
    private final WaterIntakeRepository waterIntakeRepository;
    private final EmailService emailService;
    private final TelegramService telegramService;

    @Value("${app.reminder.interval.hours}")
    private long reminderIntervalHours;

    @Override
    public void sendHydrationReminders() {
        log.info("Hydration reminder scheduler started.");

        List<User> users = userRepository.findAllByEmailNotificationEnabledTrueOrTelegramNotificationEnabledTrue();

        log.info("Found {} users eligible for notifications.", users.size());

        for (User user : users) {

            ZoneId zone;

            try {

                zone = ZoneId.of(user.getTimezone());

            } catch (Exception ex) {

                zone = ZoneId.systemDefault();

            }

            LocalDate today = LocalDate.now(zone);

            ZonedDateTime start = today.atStartOfDay(zone);

            ZonedDateTime end = start.plusDays(1);

            LocalDateTime startTime = start.toLocalDateTime();

            LocalDateTime endTime = end.toLocalDateTime();

            List<WaterIntake> todayEntries =
                    waterIntakeRepository.findAllByUserAndConsumedAtBetween(
                            user,
                            startTime,
                            endTime
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

                if (!today.equals(user.getLastGoalNotificationDate())) {

                    if (Boolean.TRUE.equals(user.getEmailNotificationEnabled())) {

                        emailService.sendGoalAchieved(
                                user.getEmail(),
                                user.getUsername()
                        );

                    }

                    if (Boolean.TRUE.equals(user.getTelegramNotificationEnabled())
                            && user.getTelegramChatId() != null) {

                        telegramService.sendGoalAchieved(
                                user.getTelegramChatId(),
                                user.getUsername(),
                                goal
                        );

                    }

                    user.setLastGoalNotificationDate(today);

                    userRepository.save(user);

                    log.info(
                            "Goal achievement notification sent to user '{}'.",
                            user.getUsername()
                    );

                } else {

                    log.info(
                            "Skipping goal notification for '{}'. Already notified today.",
                            user.getUsername()
                    );

                }

                continue;

            }

            if (Boolean.TRUE.equals(user.getEmailNotificationEnabled())) {

                if(user.getLastReminderSentAt()!=null &&
                        user.getLastReminderSentAt()
                                .isAfter(LocalDateTime.now().minusHours(reminderIntervalHours))){

                    log.info(
                            "Skipping reminder for '{}'. Reminder sent recently.",
                            user.getUsername()
                    );
                    continue;

                }

                emailService.sendHydrationReminder(
                        user.getEmail(),
                        user.getUsername()
                );

                log.info(
                        "Hydration reminder sent to user '{}' via email notification.",
                        user.getUsername()
                );

                user.setLastReminderSentAt(LocalDateTime.now());

                userRepository.save(user);

            }

            if (Boolean.TRUE.equals(user.getTelegramNotificationEnabled())
                    && user.getTelegramChatId() != null) {

                telegramService.sendHydrationReminder(
                        user.getTelegramChatId(),
                        user.getUsername(),
                        consumed,
                        goal
                );

                log.info(
                        "Hydration reminder sent to user '{}'  via telegram notification.",
                        user.getUsername()
                );

            }
        }

        log.info("Hydration reminder scheduler finished.");
    }
}