package com.hydration.controller;

import com.hydration.dto.request.LoginRequest;
import com.hydration.dto.request.RegisterRequest;
import com.hydration.dto.response.LoginResponse;
import com.hydration.dto.response.RegisterResponse;
import com.hydration.security.JwtAuthenticationFilter;
import com.hydration.security.JwtService;
import com.hydration.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void login_shouldReturnOk() throws Exception {

        LoginResponse response =
                new LoginResponse(
                        "john123",
                        "test-jwt-token",
                        "Login successful."
                );

        when(userService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "username": "john123",
                                            "password": "password123"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john123"))
                .andExpect(jsonPath("$.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.message").value("Login successful."));

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    void login_withInvalidRequest_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "username": "",
                                            "password": ""
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturnOk() throws Exception {

        RegisterResponse response =
                new RegisterResponse(
                        "john123",
                        "User registered successfully."
                );

        when(userService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "username": "john123",
                                            "email": "john@example.com",
                                            "password": "password123"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john123"))
                .andExpect(jsonPath("$.message")
                        .value("User registered successfully."));

        verify(userService).register(any(RegisterRequest.class));
    }

    @Test
    void register_withInvalidEmail_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "username": "john123",
                                            "email": "invalid-email",
                                            "password": "password123"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());
    }
}