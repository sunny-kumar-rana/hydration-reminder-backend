package com.hydration.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    @NotBlank
    @Email
    private String email;

    @NotNull
    private Integer dailyGoal;

    @NotBlank
    private String timezone;

    @NotNull
    private Boolean emailNotificationEnabled;
}
