package com.chatchatabc.healthhealthabc.domain.service

import com.chatchatabc.healthhealthabc.domain.model.User
import org.springframework.stereotype.Service

@Service
interface JwtService {

    /**
     * Generate a token for the given user
     */
    fun generateToken(user: User, ipAddress: String): String

    /**
     * Validate the given token
     */
    fun validateTokenAndGetId(token: String): String
}