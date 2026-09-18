package com.hydration.controller;

import com.hydration.dto.request.ChangePasswordRequest;
import com.hydration.dto.request.UpdateProfileRequest;
import com.hydration.dto.response.ProfileResponse;
import com.hydration.security.JwtAuthenticationFilter;
import com.hydration.security.JwtService;
import com.hydration.service.interfaces.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService profileService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getProfile_shouldReturnOk() throws Exception {

        ProfileResponse response =
                new ProfileResponse(
                        "john123",
                        2500,
                        "john@example.com",
                        true,
                        "123456789",
                        true,
                        "Asia/Kolkata",
                        LocalDateTime.of(2026, 1, 1, 10, 0)
                );

        when(profileService.getProfile())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/profile")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john123"))
                .andExpect(jsonPath("$.dailyGoal").value(2500))
                .andExpect(jsonPath("$.email")
                        .value("john@example.com"))
                .andExpect(jsonPath("$.emailNotificationEnabled")
                        .value(true))
                .andExpect(jsonPath("$.telegramChatId")
                        .value("123456789"))
                .andExpect(jsonPath("$.telegramNotificationEnabled")
                        .value(true))
                .andExpect(jsonPath("$.timezone")
                        .value("Asia/Kolkata"));

        verify(profileService).getProfile();
    }

    @Test
    void updateProfile_shouldReturnOk() throws Exception {

        ProfileResponse response =
                new ProfileResponse(
                        "john123",
                        3000,
                        "john@example.com",
                        true,
                        "123456789",
                        false,
                        "Asia/Kolkata",
                        LocalDateTime.of(2026, 1, 1, 10, 0)
                );

        when(profileService.updateProfile(
                any(UpdateProfileRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "john@example.com",
                                            "dailyGoal": 3000,
                                            "timezone": "Asia/Kolkata",
                                            "emailNotificationEnabled": true,
                                            "telegramNotificationEnabled": false,
                                            "telegramChatId": "123456789"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john123"))
                .andExpect(jsonPath("$.dailyGoal").value(3000))
                .andExpect(jsonPath("$.telegramNotificationEnabled")
                        .value(false));

        verify(profileService)
                .updateProfile(any(UpdateProfileRequest.class));
    }

    @Test
    void updateProfile_withInvalidEmail_shouldReturnBadRequest()
            throws Exception {

        mockMvc.perform(
                        put("/api/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "invalid-email",
                                            "dailyGoal": 3000,
                                            "timezone": "Asia/Kolkata",
                                            "emailNotificationEnabled": true,
                                            "telegramNotificationEnabled": false,
                                            "telegramChatId": "123456789"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void changePassword_shouldReturnNoContent() throws Exception {

        doNothing()
                .when(profileService)
                .changePassword(any(ChangePasswordRequest.class));

        mockMvc.perform(
                        put("/api/profile/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "oldPassword": "oldpass",
                                            "newPassword": "newpass"
                                        }
                                        """)
                )
                .andExpect(status().isNoContent());

        verify(profileService)
                .changePassword(any(ChangePasswordRequest.class));
    }

    @Test
    void testEmail_shouldReturnOk() throws Exception {

        doNothing()
                .when(profileService)
                .sendTestEmail();

        mockMvc.perform(
                        post("/api/profile/test-email")
                )
                .andExpect(status().isOk())
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers
                                .content()
                                .string("Test email sent.")
                );

        verify(profileService).sendTestEmail();
    }

    @Test
    void testTelegram_shouldReturnOk() throws Exception {

        doNothing()
                .when(profileService)
                .sendTestTelegram();

        mockMvc.perform(
                        post("/api/profile/test-telegram")
                )
                .andExpect(status().isOk())
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers
                                .content()
                                .string("Test Telegram notification sent.")
                );

        verify(profileService).sendTestTelegram();
    }
}