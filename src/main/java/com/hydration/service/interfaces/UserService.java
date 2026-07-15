package com.hydration.service.interfaces;

import com.hydration.dto.request.LoginRequest;
import com.hydration.dto.request.RegisterRequest;
import com.hydration.dto.response.LoginResponse;
import com.hydration.dto.response.RegisterResponse;

public interface UserService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
