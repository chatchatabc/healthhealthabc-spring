package com.chatchatabc.api.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginRequest implements Serializable {
    private String username;
    private String password;
}
