package com.chatchatabc.doctor.impl.domain.service

import com.chatchatabc.doctor.domain.service.JwtService
import org.springframework.stereotype.Service

@Service
class JwtServiceImpl : JwtService {
    /**
    * Generate a token for the given user
    */
    override fun generateToken(userId: String, ipAddress: String): String {
        TODO("Not yet implemented")
    }

    /**
    * Validate the given token
    */
    override fun validateTokenAndGetId(token: String): String {
        TODO("Not yet implemented")
    }
}