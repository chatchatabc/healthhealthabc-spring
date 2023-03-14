package com.chatchatabc.healthhealthabc.domain.repository

import com.chatchatabc.healthhealthabc.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, String> {

    /**
     * Find a user by their username.
     */
    fun findByUsername(username: String?): Optional<User>

    /**
     * Find a user by their email.
     */
    fun findByEmail(email: String?): Optional<User>
}