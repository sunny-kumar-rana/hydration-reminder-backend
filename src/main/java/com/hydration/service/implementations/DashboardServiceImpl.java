package com.hydration.service.implementations;

import com.hydration.dto.response.*;
import com.hydration.entity.User;
import com.hydration.repository.UserRepository;
import com.hydration.repository.WaterIntakeRepository;
import com.hydration.service.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public List<WeeklyProgressResponse> getWeeklyProgress() {
        return null;
    }

    @Override
    public List<MonthlyProgressResponse> getMonthlyProgress() {
        return null;
    }

    @Override
    public StreakResponse getStreak() {
        return null;
    }
}