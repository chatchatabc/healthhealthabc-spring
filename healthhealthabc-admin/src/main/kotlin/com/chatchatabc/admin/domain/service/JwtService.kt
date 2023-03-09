package com.chatchatabc.admin.domain.service

import org.springframework.stereotype.Service

@Service
interface JwtService {

        /**
        * Generate a token for the given user
        */
        fun generateToken(userId: String, ipAddress: String): String

        /**
        * Validate the given token
        */
        fun validateTokenAndGetId(token: String): String
}