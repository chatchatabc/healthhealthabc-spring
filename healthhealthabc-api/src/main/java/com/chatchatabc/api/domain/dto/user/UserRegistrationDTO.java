package com.chatchatabc.api.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonTypeName("userRegistration")
public class UserRegistrationDTO implements Serializable {
    private String email;
    private String username;
    private String password;
}
