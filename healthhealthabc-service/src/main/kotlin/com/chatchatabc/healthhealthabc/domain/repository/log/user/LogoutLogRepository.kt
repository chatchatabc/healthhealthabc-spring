package com.chatchatabc.healthhealthabc.domain.repository.log.user

import com.chatchatabc.healthhealthabc.domain.model.log.user.LogoutLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogoutLogRepository : JpaRepository<LogoutLog, Long> {
}