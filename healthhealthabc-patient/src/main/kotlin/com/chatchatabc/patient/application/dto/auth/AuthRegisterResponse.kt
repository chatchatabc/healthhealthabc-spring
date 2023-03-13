package com.chatchatabc.patient.application.dto.auth

import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.patient.application.dto.ErrorContent

data class AuthRegisterResponse(var user: UserDTO?, var errorContent: ErrorContent?) {
}