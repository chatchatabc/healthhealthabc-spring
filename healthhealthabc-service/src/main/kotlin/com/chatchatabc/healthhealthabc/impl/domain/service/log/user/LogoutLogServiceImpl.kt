package com.chatchatabc.healthhealthabc.impl.domain.service.log.user

import com.chatchatabc.healthhealthabc.domain.model.User
import com.chatchatabc.healthhealthabc.domain.model.log.user.LogoutLog
import com.chatchatabc.healthhealthabc.domain.repository.UserRepository
import com.chatchatabc.healthhealthabc.domain.repository.log.user.LogoutLogRepository
import com.chatchatabc.healthhealthabc.domain.service.log.user.LogoutLogService
import org.springframework.stereotype.Service
import java.util.*

@Service
class LogoutLogServiceImpl(
    private val logoutLogRepository: LogoutLogRepository,
    private val userRepository: UserRepository
) : LogoutLogService {
    /**
     * Create a logout log of a user logout attempt.
     */
    override fun createLog(id: String, ipAddress: String): String {
        val user: Optional<User> = userRepository.findById(id)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        val logoutLog = LogoutLog()
        logoutLog.apply {
            this.user = user.get()
            this.email = user.get().email
            this.ipAddress = ipAddress
        }
        val logoutLogSaved = logoutLogRepository.save(logoutLog)
        return logoutLogSaved.id
    }
}