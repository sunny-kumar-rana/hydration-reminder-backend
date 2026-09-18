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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaterServiceImplTest {

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @Mock
    private WaterIntakeRepository waterIntakeRepository;

    @InjectMocks
    private WaterServiceImpl waterService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setDailyGoal(3000);
        user.setTimezone("Asia/Kolkata");
    }

    @Test
    void addWater_shouldSaveWaterIntakeForCurrentUser() {
        AddWaterRequest request = new AddWaterRequest(500);

        WaterIntake saved = new WaterIntake();
        saved.setId(1L);
        saved.setUser(user);
        saved.setAmount(500);
        saved.setConsumedAt(LocalDateTime.now());

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(waterIntakeRepository.save(any(WaterIntake.class)))
                .thenReturn(saved);

        WaterResponse response = waterService.addWater(request);

        assertEquals(1L, response.getId());
        assertEquals(500, response.getAmount());
        assertNotNull(response.getConsumedAt());

        verify(waterIntakeRepository).save(argThat(intake ->
                intake.getUser() == user
                        && intake.getAmount() == 500
                        && intake.getConsumedAt() != null
        ));
    }

    @Test
    void updateWater_shouldUpdateOwnedEntry() {
        WaterIntake intake = new WaterIntake();
        intake.setId(10L);
        intake.setUser(user);
        intake.setAmount(500);
        intake.setConsumedAt(LocalDateTime.now().minusHours(1));

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(waterIntakeRepository.findByIdAndUser(10L, user))
                .thenReturn(Optional.of(intake));

        when(waterIntakeRepository.save(intake))
                .thenReturn(intake);

        WaterResponse response =
                waterService.updateWater(
                        10L,
                        new UpdateWaterRequest(750)
                );

        assertEquals(10L, response.getId());
        assertEquals(750, response.getAmount());

        verify(waterIntakeRepository).save(intake);
    }

    @Test
    void updateWater_shouldRejectEntryNotOwnedByCurrentUser() {
        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(waterIntakeRepository.findByIdAndUser(10L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                WaterIntakeNotFoundException.class,
                () -> waterService.updateWater(
                        10L,
                        new UpdateWaterRequest(750)
                )
        );

        verify(waterIntakeRepository, never()).save(any());
    }

    @Test
    void deleteWater_shouldDeleteOwnedEntry() {
        WaterIntake intake = new WaterIntake();
        intake.setId(10L);
        intake.setUser(user);
        intake.setAmount(500);

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(waterIntakeRepository.findByIdAndUser(10L, user))
                .thenReturn(Optional.of(intake));

        waterService.deleteWater(10L);

        verify(waterIntakeRepository).delete(intake);
    }

    @Test
    void deleteWater_shouldRejectEntryNotOwnedByCurrentUser() {
        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(waterIntakeRepository.findByIdAndUser(10L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                WaterIntakeNotFoundException.class,
                () -> waterService.deleteWater(10L)
        );

        verify(waterIntakeRepository, never()).delete(any());
    }

    @Test
    void getHistory_shouldReturnEntriesInRepositoryOrder() {
        WaterIntake first = createIntake(1L, 500);
        WaterIntake second = createIntake(2L, 750);

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(waterIntakeRepository
                .findAllByUserOrderByConsumedAtDesc(user))
                .thenReturn(List.of(first, second));

        List<WaterResponse> response = waterService.getHistory();

        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals(500, response.get(0).getAmount());
        assertEquals(2L, response.get(1).getId());
        assertEquals(750, response.get(1).getAmount());
    }

    @Test
    void getTodayWater_shouldReturnTodayEntries() {
        WaterIntake first = createIntake(1L, 500);
        WaterIntake second = createIntake(2L, 1000);

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(waterIntakeRepository.findAllByUserAndConsumedAtBetween(
                eq(user),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(first, second));

        List<WaterResponse> response = waterService.getTodayWater();

        assertEquals(2, response.size());
        assertEquals(500, response.get(0).getAmount());
        assertEquals(1000, response.get(1).getAmount());
    }

    @Test
    void getDailySummary_shouldCalculateConsumptionAndProgress() {
        WaterIntake first = createIntake(1L, 500);
        WaterIntake second = createIntake(2L, 1000);

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(waterIntakeRepository.findAllByUserAndConsumedAtBetween(
                eq(user),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(first, second));

        DailySummaryResponse response =
                waterService.getDailySummary();

        assertEquals(3000, response.getGoal());
        assertEquals(1500, response.getConsumed());
        assertEquals(1500, response.getRemaining());
        assertEquals(50.0, response.getProgressPercentage());
    }

    @Test
    void getDailySummary_shouldNeverReturnNegativeRemaining() {
        WaterIntake first = createIntake(1L, 2000);
        WaterIntake second = createIntake(2L, 2000);

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(waterIntakeRepository.findAllByUserAndConsumedAtBetween(
                eq(user),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(first, second));

        DailySummaryResponse response =
                waterService.getDailySummary();

        assertEquals(4000, response.getConsumed());
        assertEquals(0, response.getRemaining());
        assertEquals(
                4000 * 100.0 / 3000,
                response.getProgressPercentage()
        );
    }

    @Test
    void getDailySummary_shouldReturnZeroProgressWhenGoalIsNull() {
        user.setDailyGoal(null);

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(waterIntakeRepository.findAllByUserAndConsumedAtBetween(
                eq(user),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(createIntake(1L, 500)));

        DailySummaryResponse response =
                waterService.getDailySummary();

        assertEquals(0, response.getGoal());
        assertEquals(500, response.getConsumed());
        assertEquals(0, response.getRemaining());
        assertEquals(0.0, response.getProgressPercentage());
    }

    private WaterIntake createIntake(Long id, int amount) {
        WaterIntake intake = new WaterIntake();
        intake.setId(id);
        intake.setUser(user);
        intake.setAmount(amount);
        intake.setConsumedAt(LocalDateTime.now());
        return intake;
    }
}