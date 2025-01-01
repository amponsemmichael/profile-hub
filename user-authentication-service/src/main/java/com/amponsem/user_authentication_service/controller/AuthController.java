package com.amponsem.user_authentication_service.controller;

import com.amponsem.user_authentication_service.dto.LoginDto;
import com.amponsem.user_authentication_service.dto.PasswordResetDto;
import com.amponsem.user_authentication_service.dto.UserRegistrationDto;
import com.amponsem.user_authentication_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        authService.registerUser(registrationDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<Void> requestPasswordReset(@RequestParam String email) {
        authService.initiatePasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetDto resetDto) {
        authService.resetPassword(resetDto);
        return ResponseEntity.ok().build();
    }

}
