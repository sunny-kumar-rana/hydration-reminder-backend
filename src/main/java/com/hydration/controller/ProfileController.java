package com.hydration.controller;

import com.hydration.dto.request.ChangePasswordRequest;
import com.hydration.dto.request.UpdateProfileRequest;
import com.hydration.dto.response.ProfileResponse;
import com.hydration.service.interfaces.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        return ResponseEntity.ok(
                profileService.updateProfile(request)
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        profileService.changePassword(request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test-email")
    public ResponseEntity<String> testEmail() {

        profileService.sendTestEmail();

        return ResponseEntity.ok("Test email sent.");

    }

    @PostMapping("/test-telegram")
    public ResponseEntity<String> testTelegram() {

        profileService.sendTestTelegram();

        return ResponseEntity.ok("Test Telegram notification sent.");

    }
}