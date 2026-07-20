package com.hydration.service.implementations;

import com.hydration.dto.request.ChangePasswordRequest;
import com.hydration.dto.request.UpdateProfileRequest;
import com.hydration.dto.response.ProfileResponse;
import com.hydration.entity.User;
import com.hydration.exception.EmailAlreadyExistsException;
import com.hydration.exception.IncorrectPasswordException;
import com.hydration.repository.UserRepository;
import com.hydration.service.AuthenticatedUserService;
import com.hydration.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final AuthenticatedUserService authenticatedUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public ProfileResponse getProfile() {

        User user = authenticatedUserService.getCurrentUser();

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

        User user = authenticatedUserService.getCurrentUser();

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
}
