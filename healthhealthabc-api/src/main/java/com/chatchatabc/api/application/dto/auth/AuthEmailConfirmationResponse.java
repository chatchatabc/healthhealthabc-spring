package com.chatchatabc.api.application.dto.auth;

import com.chatchatabc.api.application.dto.ErrorContent;
import com.chatchatabc.api.application.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthEmailConfirmationResponse {
    private UserDTO user;
    private ErrorContent errorContent;
}
