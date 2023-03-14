package com.chatchatabc.api.application.dto.auth;

import com.chatchatabc.api.application.dto.ErrorContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResetPasswordResponse {
    private String email;
    private String username;
    private ErrorContent errorContent;
}
