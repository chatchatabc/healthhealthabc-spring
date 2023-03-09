package com.chatchatabc.healthhealthabc.application.dto.auth

import com.chatchatabc.api.domain.dto.user.UserDTO
import com.chatchatabc.healthhealthabc.application.dto.ErrorContent

data class RegisterResponse(var user: UserDTO?, var errorContent: ErrorContent?) {
}