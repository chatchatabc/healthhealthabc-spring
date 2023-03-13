package com.chatchatabc.healthhealthabc.application.dto.auth

import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.healthhealthabc.application.dto.ErrorContent

data class LoginResponse(var user: UserDTO?, var errorContent: ErrorContent?) {
}