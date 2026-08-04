package com.hydration.repository;

import com.hydration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
    List<User> findAllByEmailNotificationEnabledTrue();
    List<User> findAllByEmailNotificationEnabledTrueOrTelegramNotificationEnabledTrue();
}