package com.amponsem.user_authentication_service.dto;

import lombok.Data;

@Data
public class PasswordResetDto {
    private String email;
    private String token;
    private String newPassword;
}

