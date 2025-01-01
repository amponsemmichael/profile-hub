package com.amponsem.user_authentication_service.service;

import com.amponsem.user_authentication_service.dto.LoginDto;
import com.amponsem.user_authentication_service.dto.PasswordResetDto;
import com.amponsem.user_authentication_service.dto.UserRegistrationDto;
import com.amponsem.user_authentication_service.repository.UserRepository;
import com.amponsem.user_authentication_service.security.CustomUserDetailsService;
import com.amponsem.user_authentication_service.security.JwtTokenProvider;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.amponsem.user_authentication_service.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    @CircuitBreaker(name = "registerUser", fallbackMethod = "registerUserFallback")
    public void registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        userRepository.save(user);
    }

    @CircuitBreaker(name = "login", fallbackMethod = "loginFallback")
    public String login(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            // Generate new token
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
            return jwtTokenProvider.generateToken(userDetails);
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    @Transactional
    @CircuitBreaker(name = "initiatePasswordReset", fallbackMethod = "initiatePasswordResetFallback")
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(System.currentTimeMillis() + 3600000); // 1 hour validity

        userRepository.save(user);
    }

    @Transactional
    @CircuitBreaker(name = "resetPassword", fallbackMethod = "resetPasswordFallback")
    public void resetPassword(PasswordResetDto resetDto) {
        User user = userRepository.findByResetToken(resetDto.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry() < System.currentTimeMillis()) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(resetDto.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    private void registerUserFallback(UserRegistrationDto registrationDto, Throwable t) {
        throw new RuntimeException("Registration service is currently unavailable");
    }

    private String loginFallback(LoginDto loginDto, Throwable t) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
            return jwtTokenProvider.generateToken(userDetails);
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    private void initiatePasswordResetFallback(String email, Throwable t) {
        throw new RuntimeException("Password reset initiation service is currently unavailable: " + t.getMessage());
    }
}
