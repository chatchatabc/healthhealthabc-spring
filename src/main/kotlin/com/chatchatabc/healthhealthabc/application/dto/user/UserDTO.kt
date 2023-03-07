package com.chatchatabc.healthhealthabc.application.dto.user

import java.time.Instant

data class UserDTO(val id: String?, val email: String?, val username: String?, val emailConfirmedAt: Instant?, val createdAt: Instant?, val updatedAt: Instant?) {
}