package com.chatchatabc.healthhealthabc.domain.service.log.user

import org.springframework.stereotype.Service

@Service
interface LogoutLogService {

    /**
     * Create a logout log of a user logout attempt.
     */
    fun createLog(id: String, ipAddress: String): String
}