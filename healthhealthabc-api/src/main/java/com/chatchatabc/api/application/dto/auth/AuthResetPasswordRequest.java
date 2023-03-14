package com.chatchatabc.api.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResetPasswordRequest {
    private String email;
    private String password;
    private String recoveryCode;
}
