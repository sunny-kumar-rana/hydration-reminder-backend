package com.hydration.service.implementations;

import com.hydration.dto.request.AddWaterRequest;
import com.hydration.dto.request.UpdateWaterRequest;
import com.hydration.dto.response.DailySummaryResponse;
import com.hydration.dto.response.WaterResponse;
import com.hydration.entity.User;
import com.hydration.entity.WaterIntake;
import com.hydration.exception.InvalidCredentialsException;
import com.hydration.exception.WaterIntakeNotFoundException;
import com.hydration.repository.UserRepository;
import com.hydration.repository.WaterIntakeRepository;
import com.hydration.service.interfaces.WaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterServiceImpl implements WaterService {

    private final UserRepository userRepository;
    private final WaterIntakeRepository waterIntakeRepository;

    @Override
    public WaterResponse addWater(AddWaterRequest request) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

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

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    @Override
    public WaterResponse updateWater(Long id, UpdateWaterRequest request) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

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

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        WaterIntake intake = waterIntakeRepository
                .findByIdAndUser(id, user)
                .orElseThrow(WaterIntakeNotFoundException::new);

        waterIntakeRepository.delete(intake);
    }

    @Override
    public List<WaterResponse> getTodayWater() {
        return List.of();
    }

    @Override
    public List<WaterResponse> getHistory() {
        return List.of();
    }

    @Override
    public DailySummaryResponse getDailySummary() {
        return null;
    }
}
