package com.hydration.service;

import com.hydration.dto.RegisterRequest;
import com.hydration.dto.RegisterResponse;
import com.hydration.entity.User;
import com.hydration.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public RegisterResponse register(RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("username already exists");
        }if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("e-mail already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setUsername(user.getUsername());
        response.setMessage("user "+user.getUsername()+" Registered Successfully");

        return response;
    }
}
