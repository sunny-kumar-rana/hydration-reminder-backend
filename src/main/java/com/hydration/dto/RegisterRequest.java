package com.hydration.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "username cannot be empty")
    @Size(min = 5, max = 20, message = "username must be between 5 to 20 characters")
    private String username;

    @NotBlank(message = "e-mail cannot be empty")
    @Email(message = "please provide a valid e-mail address")
    private String email;

    @NotBlank(message = "password can't be empty")
    @Size(min = 6, max = 16, message = "password must be between 6 to 15 characters")
    private String password;
}
