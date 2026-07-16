package com.hydration.service.implementations;

import com.hydration.dto.request.ChangePasswordRequest;
import com.hydration.dto.request.UpdateProfileRequest;
import com.hydration.dto.response.ProfileResponse;
import com.hydration.entity.User;
import com.hydration.exception.EmailAlreadyExistsException;
import com.hydration.exception.IncorrectPasswordException;
import com.hydration.exception.InvalidCredentialsException;
import com.hydration.repository.UserRepository;
import com.hydration.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return authentication.getName();
    }

    @Override
    public ProfileResponse getProfile() {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        return new ProfileResponse(
                user.getUsername(),
                user.getEmail(),
                user.getDailyGoal(),
                user.getTelegramChatId(),
                user.getEmailNotificationEnabled(),
                user.getTelegramNotificationEnabled(),
                user.getTimezone(),
                user.getCreatedAt()
        );
    }

    @Override
    public ProfileResponse updateProfile(UpdateProfileRequest request) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        user.setEmail(request.getEmail());
        user.setDailyGoal(request.getDailyGoal());
        user.setTimezone(request.getTimezone());

        User updatedUser = userRepository.save(user);

        return new ProfileResponse(
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getDailyGoal(),
                updatedUser.getTelegramChatId(),
                updatedUser.getEmailNotificationEnabled(),
                updatedUser.getTelegramNotificationEnabled(),
                updatedUser.getTimezone(),
                updatedUser.getCreatedAt()
        );
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            throw new IncorrectPasswordException();
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);
    }
}
