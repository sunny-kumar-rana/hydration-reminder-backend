package com.hydration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HydrationReminderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydrationReminderApplication.class, args);
    }

}