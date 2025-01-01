package com.amponsem.user_authentication_service.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
}
