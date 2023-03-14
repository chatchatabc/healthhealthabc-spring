package com.chatchatabc.api.application.dto.auth;

import com.chatchatabc.api.application.dto.ErrorContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUsernameCheckResponse {
    private boolean taken;
    private ErrorContent errorContent;
}
