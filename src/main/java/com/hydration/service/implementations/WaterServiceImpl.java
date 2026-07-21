package com.hydration.service.implementations;

import com.hydration.dto.request.AddWaterRequest;
import com.hydration.dto.request.UpdateWaterRequest;
import com.hydration.dto.response.DailySummaryResponse;
import com.hydration.dto.response.WaterResponse;
import com.hydration.entity.User;
import com.hydration.entity.WaterIntake;
import com.hydration.exception.WaterIntakeNotFoundException;
import com.hydration.repository.WaterIntakeRepository;
import com.hydration.service.AuthenticatedUserService;
import com.hydration.service.interfaces.WaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterServiceImpl implements WaterService {

    private final AuthenticatedUserService authenticatedUserService;
    private final WaterIntakeRepository waterIntakeRepository;

    @Override
    public WaterResponse addWater(AddWaterRequest request) {

        User user = authenticatedUserService.getCurrentUser();

        WaterIntake intake = new WaterIntake();

        intake.setUser(user);
        intake.setAmount(request.getAmount());
        intake.setConsumedAt(LocalDateTime.now());

        WaterIntake saved = waterIntakeRepository.save(intake);

        return new WaterResponse(
                saved.getId(),
                saved.getAmount(),
                saved.getConsumedAt()
        );
    }

    @Override
    public WaterResponse updateWater(Long id, UpdateWaterRequest request) {

        User user = authenticatedUserService.getCurrentUser();

        WaterIntake intake = waterIntakeRepository
                .findByIdAndUser(id, user)
                .orElseThrow(WaterIntakeNotFoundException::new);

        intake.setAmount(request.getAmount());

        WaterIntake updated = waterIntakeRepository.save(intake);

        return new WaterResponse(
                updated.getId(),
                updated.getAmount(),
                updated.getConsumedAt()
        );
    }

    @Override
    public void deleteWater(Long id) {

        User user = authenticatedUserService.getCurrentUser();

        WaterIntake intake = waterIntakeRepository
                .findByIdAndUser(id, user)
                .orElseThrow(WaterIntakeNotFoundException::new);

        waterIntakeRepository.delete(intake);
    }

    @Override
    public List<WaterResponse> getTodayWater() {

        User user = authenticatedUserService.getCurrentUser();

        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return waterIntakeRepository
                .findAllByUserAndConsumedAtBetween(user, start, end)
                .stream()
                .map(water -> new WaterResponse(
                        water.getId(),
                        water.getAmount(),
                        water.getConsumedAt()
                ))
                .toList();
    }

    @Override
    public List<WaterResponse> getHistory() {

        User user = authenticatedUserService.getCurrentUser();

        return waterIntakeRepository
                .findAllByUserOrderByConsumedAtDesc(user)
                .stream()
                .map(water -> new WaterResponse(
                        water.getId(),
                        water.getAmount(),
                        water.getConsumedAt()
                ))
                .toList();
    }

    @Override
    public DailySummaryResponse getDailySummary() {

        User user = authenticatedUserService.getCurrentUser();

        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        List<WaterIntake> waterList =
                waterIntakeRepository.findAllByUserAndConsumedAtBetween(
                        user,
                        start,
                        end
                );

        int consumed = waterList.stream()
                .mapToInt(WaterIntake::getAmount)
                .sum();

        int goal = user.getDailyGoal() == null
                ? 0
                : user.getDailyGoal();

        int remaining = Math.max(goal - consumed, 0);

        double progress =
                goal == 0
                        ? 0
                        : (consumed * 100.0) / goal;

        return new DailySummaryResponse(
                today,
                goal,
                consumed,
                remaining,
                progress
        );
    }
}
