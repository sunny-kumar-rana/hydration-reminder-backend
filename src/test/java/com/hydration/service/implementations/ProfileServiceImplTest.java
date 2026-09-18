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
import com.hydration.service.interfaces.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();

        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encoded-old-password");
        user.setDailyGoal(3000);
        user.setEmailNotificationEnabled(true);
        user.setTelegramNotificationEnabled(true);
        user.setTelegramChatId("123456789");
        user.setTimezone("Asia/Kolkata");
    }

    @Test
    void getProfile_shouldReturnCurrentUserProfile() {
        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        ProfileResponse response = profileService.getProfile();

        assertEquals("testuser", response.getUsername());
        assertEquals(3000, response.getDailyGoal());
        assertEquals("test@example.com", response.getEmail());
        assertTrue(response.getEmailNotificationEnabled());
        assertEquals("123456789", response.getTelegramChatId());
        assertTrue(response.getTelegramNotificationEnabled());
        assertEquals("Asia/Kolkata", response.getTimezone());
    }

    @Test
    void updateProfile_shouldUpdateUserDetails() {
        UpdateProfileRequest request =
                new UpdateProfileRequest(
                        "new@example.com",
                        3500,
                        "Asia/Kolkata",
                        false,
                        true,
                        "987654321"
                );

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(userRepository.existsByEmail("new@example.com"))
                .thenReturn(false);

        when(userRepository.save(user))
                .thenReturn(user);

        ProfileResponse response =
                profileService.updateProfile(request);

        assertEquals("new@example.com", response.getEmail());
        assertEquals(3500, response.getDailyGoal());
        assertEquals("Asia/Kolkata", response.getTimezone());
        assertFalse(response.getEmailNotificationEnabled());
        assertTrue(response.getTelegramNotificationEnabled());
        assertEquals("987654321", response.getTelegramChatId());

        verify(userRepository).save(user);
    }

    @Test
    void updateProfile_shouldRejectDuplicateEmail() {
        UpdateProfileRequest request =
                new UpdateProfileRequest(
                        "taken@example.com",
                        3500,
                        "Asia/Kolkata",
                        true,
                        true,
                        "123456789"
                );

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(userRepository.existsByEmail("taken@example.com"))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> profileService.updateProfile(request)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateProfile_shouldConvertBlankTelegramChatIdToNull() {
        UpdateProfileRequest request =
                new UpdateProfileRequest(
                        "test@example.com",
                        3000,
                        "Asia/Kolkata",
                        true,
                        false,
                        "   "
                );

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(userRepository.save(user))
                .thenReturn(user);

        ProfileResponse response =
                profileService.updateProfile(request);

        assertNull(response.getTelegramChatId());
        assertFalse(response.getTelegramNotificationEnabled());

        verify(userRepository).save(user);
    }

    @Test
    void changePassword_shouldEncodeAndSaveNewPassword() {
        ChangePasswordRequest request =
                new ChangePasswordRequest(
                        "old-password",
                        "new-password"
                );

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(passwordEncoder.matches(
                "old-password",
                "encoded-old-password"
        )).thenReturn(true);

        when(passwordEncoder.encode("new-password"))
                .thenReturn("encoded-new-password");

        when(userRepository.save(user))
                .thenReturn(user);

        profileService.changePassword(request);

        assertEquals(
                "encoded-new-password",
                user.getPassword()
        );

        verify(passwordEncoder).matches(
                "old-password",
                "encoded-old-password"
        );

        verify(passwordEncoder).encode("new-password");
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_shouldRejectIncorrectOldPassword() {
        ChangePasswordRequest request =
                new ChangePasswordRequest(
                        "wrong-password",
                        "new-password"
                );

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        when(passwordEncoder.matches(
                "wrong-password",
                "encoded-old-password"
        )).thenReturn(false);

        assertThrows(
                IncorrectPasswordException.class,
                () -> profileService.changePassword(request)
        );

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void sendTestEmail_shouldSendToCurrentUser() {
        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        profileService.sendTestEmail();

        verify(emailService).sendTestEmail(
                "test@example.com",
                "testuser"
        );
    }

    @Test
    void sendTestTelegram_shouldSendWhenTelegramIsConfigured() {
        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        profileService.sendTestTelegram();

        verify(telegramService).sendTestMessage(
                "123456789",
                "testuser"
        );
    }

    @Test
    void sendTestTelegram_shouldRejectWhenNotificationsAreDisabled() {
        user.setTelegramNotificationEnabled(false);

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        assertThrows(
                RuntimeException.class,
                () -> profileService.sendTestTelegram()
        );

        verifyNoInteractions(telegramService);
    }

    @Test
    void sendTestTelegram_shouldRejectWhenChatIdIsMissing() {
        user.setTelegramNotificationEnabled(true);
        user.setTelegramChatId(null);

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);

        assertThrows(
                RuntimeException.class,
                () -> profileService.sendTestTelegram()
        );

        verifyNoInteractions(telegramService);
    }
}