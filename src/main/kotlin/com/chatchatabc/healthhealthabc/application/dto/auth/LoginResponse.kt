package com.chatchatabc.healthhealthabc.application.dto.auth

data class LoginResponse(var token: String, var username: String, var email: String, var role: String) {
}