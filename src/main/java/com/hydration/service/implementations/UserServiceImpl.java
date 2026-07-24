package com.hydration.service.implementations;

import com.hydration.dto.request.LoginRequest;
import com.hydration.dto.response.LoginResponse;
import com.hydration.dto.request.RegisterRequest;
import com.hydration.dto.response.RegisterResponse;
import com.hydration.entity.User;
import com.hydration.exception.EmailAlreadyExistsException;
import com.hydration.exception.InvalidCredentialsException;
import com.hydration.exception.UsernameAlreadyExistsException;
import com.hydration.repository.UserRepository;
import com.hydration.security.JwtService;
import com.hydration.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

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
        user.setTimezone("Asia/Kolkata");
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
