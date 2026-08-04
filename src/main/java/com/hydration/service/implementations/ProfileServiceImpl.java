package com.hydration.service.implementations;

import com.hydration.dto.request.ChangePasswordRequest;
import com.hydration.dto.request.UpdateProfileRequest;
import com.hydration.dto.response.ProfileResponse;
import com.hydration.entity.User;
import com.hydration.exception.EmailAlreadyExistsException;
import com.hydration.exception.IncorrectPasswordException;
import com.hydration.repository.UserRepository;
import com.hydration.service.AuthenticatedUserService;
import com.hydration.service.interfaces.EmailService;
import com.hydration.service.interfaces.ProfileService;
import com.hydration.service.interfaces.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final AuthenticatedUserService authenticatedUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TelegramService telegramService;

    @Override
    public ProfileResponse getProfile() {

        User user = authenticatedUserService.getCurrentUser();

        return new ProfileResponse(
                user.getUsername(),
                user.getDailyGoal(),
                user.getEmail(),
                user.getEmailNotificationEnabled(),
                user.getTelegramChatId(),
                user.getTelegramNotificationEnabled(),
                user.getTimezone(),
                user.getCreatedAt()
        );
    }

    @Override
    public ProfileResponse updateProfile(UpdateProfileRequest request) {

        User user = authenticatedUserService.getCurrentUser();

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        user.setDailyGoal(request.getDailyGoal());
        user.setTimezone(request.getTimezone());
        user.setEmail(request.getEmail());
        user.setEmailNotificationEnabled(request.getEmailNotificationEnabled());
        user.setTelegramChatId(request.getTelegramChatId());
        user.setTelegramNotificationEnabled(request.getTelegramNotificationEnabled());

        User updatedUser = userRepository.save(user);

        return new ProfileResponse(
                updatedUser.getUsername(),
                updatedUser.getDailyGoal(),
                updatedUser.getEmail(),
                updatedUser.getEmailNotificationEnabled(),
                updatedUser.getTelegramChatId(),
                updatedUser.getTelegramNotificationEnabled(),
                updatedUser.getTimezone(),
                updatedUser.getCreatedAt()
        );
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = authenticatedUserService.getCurrentUser();

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

    @Override
    public void sendTestEmail() {

        User user = authenticatedUserService.getCurrentUser();

        emailService.sendTestEmail(
                user.getEmail(),
                user.getUsername()
        );

    }

    @Override
    public void sendTestTelegram() {

        User user = authenticatedUserService.getCurrentUser();

        if (!Boolean.TRUE.equals(user.getTelegramNotificationEnabled())) {

            throw new RuntimeException(
                    "Telegram notifications are disabled."
            );

        }

        if (user.getTelegramChatId() == null) {

            throw new RuntimeException(
                    "Telegram Chat ID not configured."
            );

        }

        telegramService.sendTestMessage(
                user.getTelegramChatId(),
                user.getUsername()
        );

    }
}
