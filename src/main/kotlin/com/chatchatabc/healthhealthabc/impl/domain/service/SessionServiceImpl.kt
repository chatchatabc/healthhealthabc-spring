package com.chatchatabc.healthhealthabc.impl.domain.service

import com.chatchatabc.healthhealthabc.domain.model.Session
import com.chatchatabc.healthhealthabc.domain.model.User
import com.chatchatabc.healthhealthabc.domain.repository.SessionRepository
import com.chatchatabc.healthhealthabc.domain.service.SessionService
import org.springframework.stereotype.Service

@Service
class SessionServiceImpl (
    private val sessionRepository: SessionRepository
) : SessionService {

    /**
     * Create a session for the given user
     */
    override fun createSession(user: User, ipAddress: String): String {
        val session = Session()
        session.apply {
            this.user = user
            this.ipAddress = ipAddress
        }
        val createdSession = sessionRepository.save(session)
        return createdSession.id!!
    }
}