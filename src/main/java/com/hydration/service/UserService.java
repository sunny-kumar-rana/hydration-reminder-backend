package com.hydration.service;

import com.hydration.dto.LoginRequest;
import com.hydration.dto.LoginResponse;
import com.hydration.dto.RegisterRequest;
import com.hydration.dto.RegisterResponse;
import com.hydration.entity.User;
import com.hydration.exception.EmailAlreadyExistsException;
import com.hydration.exception.InvalidCredentialsException;
import com.hydration.exception.UsernameAlreadyExistsException;
import com.hydration.repository.UserRepository;
import com.hydration.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public RegisterResponse register(RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new UsernameAlreadyExistsException();
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException();
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setUsername(savedUser.getUsername());
        response.setMessage("user "+savedUser.getUsername()+" Registered Successfully");

        return response;
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException();
        }
        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(
                user.getUsername(),
                token,
                "Login successful"
        );
    }
}
