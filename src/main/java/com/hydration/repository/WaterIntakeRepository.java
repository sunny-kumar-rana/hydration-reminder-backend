package com.hydration.repository;

import com.hydration.entity.User;
import com.hydration.entity.WaterIntake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaterIntakeRepository extends JpaRepository<WaterIntake, Long> {

    List<WaterIntake> findAllByUserOrderByConsumedAtDesc(User user);

    List<WaterIntake> findAllByUserAndConsumedAtBetween(User user, LocalDateTime start, LocalDateTime end);

    Optional<WaterIntake> findByIdAndUser(Long id, User user);

    void deleteByIdAndUser(Long id, User user);

    List<WaterIntake> findAllByUserAndConsumedAtBetweenOrderByConsumedAtAsc(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );

    List<WaterIntake> findAllByUserOrderByConsumedAtAsc(User user);
}