package com.hydration.scheduler;

import com.hydration.service.interfaces.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HydrationReminderScheduler {

    private final ReminderService reminderService;

    @Scheduled(cron = "0 0 8-20/2 * * *")
    public void scheduleHydrationReminders() {
        reminderService.sendHydrationReminders();
    }
}