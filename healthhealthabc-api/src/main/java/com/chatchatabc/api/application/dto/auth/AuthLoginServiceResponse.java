package com.chatchatabc.api.application.dto.auth;

import com.chatchatabc.api.application.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginServiceResponse implements Serializable {
    private String token;
    private UserDTO user;
}
