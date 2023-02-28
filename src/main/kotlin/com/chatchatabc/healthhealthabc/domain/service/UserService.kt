package com.chatchatabc.healthhealthabc.domain.service

import com.chatchatabc.healthhealthabc.domain.model.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
interface UserService : UserDetailsService {

    /**
     * Register a new user.
     */
    fun register(user: User) : User;
}