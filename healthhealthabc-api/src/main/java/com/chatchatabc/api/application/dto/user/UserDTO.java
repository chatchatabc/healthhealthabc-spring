package com.chatchatabc.api.application.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class UserDTO implements Serializable {
    private String id;
    private String email;
    private String username;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant emailConfirmedAt;
    // TODO: RoleDTO
}
