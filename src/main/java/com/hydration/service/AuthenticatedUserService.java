package com.hydration.service;

import com.hydration.entity.User;
import com.hydration.exception.InvalidCredentialsException;
import com.hydration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);
    }

    public String getCurrentUsername() {

        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}