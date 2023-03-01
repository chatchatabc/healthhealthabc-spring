package com.chatchatabc.healthhealthabc.domain.service

import com.chatchatabc.healthhealthabc.domain.model.User
import org.springframework.stereotype.Service

@Service
interface SessionService {

    /**
     * Create a session for the given user
     */
    fun createSession(user: User, ipAddress: String): String
}
