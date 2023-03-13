package com.chatchatabc.api.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO implements Serializable {
    private String email;
    private String username;
    private String password;
}
