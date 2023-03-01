package com.chatchatabc.healthhealthabc.domain.service.log.user

import com.chatchatabc.healthhealthabc.domain.model.User
import org.springframework.stereotype.Service

@Service
interface LoginLogService {

    /**
     * Create a login log of a user login attempt.
     */
    fun createLog(user: User, success: Boolean, email: String, ipAddress: String)
}