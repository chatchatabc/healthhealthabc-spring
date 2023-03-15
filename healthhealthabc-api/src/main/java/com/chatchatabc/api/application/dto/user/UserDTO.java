package com.chatchatabc.api.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private String id;
    private String email;
    private String username;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant emailConfirmedAt;
    private Set<RoleDTO> roles;
}
