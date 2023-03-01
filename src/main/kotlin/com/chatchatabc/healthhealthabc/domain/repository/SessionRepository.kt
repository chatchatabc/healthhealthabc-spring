package com.chatchatabc.healthhealthabc.domain.repository

import com.chatchatabc.healthhealthabc.domain.model.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionRepository : JpaRepository<Session, String> {
}