package com.chatchatabc.healthhealthabc.application.dto.auth

data class ResetPasswordRequest(val email: String, val password: String, val recoveryCode: String) {
}