package com.hydration.service.implementations;

import com.hydration.dto.request.LoginRequest;
import com.hydration.dto.request.RegisterRequest;
import com.hydration.dto.response.LoginResponse;
import com.hydration.dto.response.RegisterResponse;
import com.hydration.entity.User;
import com.hydration.exception.EmailAlreadyExistsException;
import com.hydration.exception.InvalidCredentialsException;
import com.hydration.exception.UsernameAlreadyExistsException;
import com.hydration.repository.UserRepository;
import com.hydration.security.JwtService;
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
class UserServiceImplTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");
        user.setTimezone("Asia/Kolkata");
    }

    @Test
    void register_shouldCreateUserWithEncodedPassword() {
        RegisterRequest request =
                new RegisterRequest(
                        "testuser",
                        "test@example.com",
                        "password123"
                );

        when(userRepository.existsByUsername("testuser"))
                .thenReturn(false);

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        RegisterResponse response = userService.register(request);

        assertEquals("testuser", response.getUsername());
        assertEquals(
                "user testuser Registered Successfully",
                response.getMessage()
        );

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getUsername().equals("testuser")
                        && savedUser.getEmail().equals("test@example.com")
                        && savedUser.getPassword().equals("encoded-password")
                        && savedUser.getTimezone().equals("Asia/Kolkata")
        ));
    }

    @Test
    void register_shouldRejectDuplicateUsername() {
        RegisterRequest request =
                new RegisterRequest(
                        "testuser",
                        "test@example.com",
                        "password123"
                );

        when(userRepository.existsByUsername("testuser"))
                .thenReturn(true);

        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> userService.register(request)
        );

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void register_shouldRejectDuplicateEmail() {
        RegisterRequest request =
                new RegisterRequest(
                        "testuser",
                        "test@example.com",
                        "password123"
                );

        when(userRepository.existsByUsername("testuser"))
                .thenReturn(false);

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.register(request)
        );

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void login_shouldReturnJwtTokenForValidCredentials() {
        LoginRequest request =
                new LoginRequest("testuser", "password123");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password123",
                "encoded-password"
        )).thenReturn(true);

        when(jwtService.generateToken("testuser"))
                .thenReturn("jwt-token");

        LoginResponse response = userService.login(request);

        assertEquals("testuser", response.getUsername());
        assertEquals("jwt-token", response.getToken());
        assertEquals("Login successful", response.getMessage());

        verify(jwtService).generateToken("testuser");
    }

    @Test
    void login_shouldRejectUnknownUsername() {
        LoginRequest request =
                new LoginRequest("unknown", "password123");

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.login(request)
        );

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void login_shouldRejectIncorrectPassword() {
        LoginRequest request =
                new LoginRequest("testuser", "wrong-password");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrong-password",
                "encoded-password"
        )).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.login(request)
        );

        verify(jwtService, never()).generateToken(anyString());
    }
}