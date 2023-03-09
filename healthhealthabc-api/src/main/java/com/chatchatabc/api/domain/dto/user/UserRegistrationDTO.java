package com.chatchatabc.api.domain.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegistrationDTO implements Serializable {
    private String email;
    private String username;
    private String password;
}
