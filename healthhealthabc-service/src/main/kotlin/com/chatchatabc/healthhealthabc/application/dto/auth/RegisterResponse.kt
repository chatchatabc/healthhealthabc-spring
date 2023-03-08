package com.chatchatabc.healthhealthabc.application.dto.auth

import com.chatchatabc.healthhealthabc.application.dto.ErrorContent
import com.chatchatabc.healthhealthabc.domain.model.User

data class RegisterResponse(var user: User?, var errorContent: ErrorContent?) {
}