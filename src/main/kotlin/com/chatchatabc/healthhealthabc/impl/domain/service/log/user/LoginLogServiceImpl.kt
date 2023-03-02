package com.chatchatabc.healthhealthabc.impl.domain.service.log.user

import com.chatchatabc.healthhealthabc.domain.model.User
import com.chatchatabc.healthhealthabc.domain.model.log.user.LoginLog
import com.chatchatabc.healthhealthabc.domain.repository.log.user.LoginLogRepository
import com.chatchatabc.healthhealthabc.domain.service.log.user.LoginLogService
import org.springframework.stereotype.Service

@Service
class LoginLogServiceImpl(
    private val loginLogRepository: LoginLogRepository
) : LoginLogService {
    /**
     * Create a login log of a user login attempt.
     */
    override fun createLog(user: User, success: Boolean, email: String, ipAddress: String): String {
        val loginLog = LoginLog()
        loginLog.apply {
            this.user = user
            this.success = success
            this.email = email
            this.ipAddress = ipAddress
        }
        val loginLogSaved = loginLogRepository.save(loginLog)
        return loginLogSaved.id!!
    }
}