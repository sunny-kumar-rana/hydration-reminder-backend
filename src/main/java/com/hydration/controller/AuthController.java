package com.hydration.controller;

import com.hydration.dto.request.LoginRequest;
import com.hydration.dto.request.RegisterRequest;
import com.hydration.dto.response.LoginResponse;
import com.hydration.dto.response.RegisterResponse;
import com.hydration.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = userService.login(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/register")
    public RegisterResponse registerUser(@RequestBody @Valid RegisterRequest request){
        return userService.register(request);
    }
}
