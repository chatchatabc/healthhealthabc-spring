package com.chatchatabc.healthhealthabc.domain.service

import com.chatchatabc.healthhealthabc.domain.model.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
interface UserService : UserDetailsService {

    /**
     * Register a new user.
     */
    fun register(user: User, roleName: String = "ROLE_PATIENT"): User

    /**
     * Confirm a user's registration.
     */
    fun confirmRegistration(emailConfirmationId: String): User

    /**
     * Generate recovery code for user to use.
     */
    fun forgotPassword(email: String): User

    /**
     * Reset user's password.
     */
    fun resetPassword(email: String, password: String, recoveryCode: String): User

    /**
     * Change user's password.
     */
    fun changePassword(id: String, oldPassword: String, newPassword: String)

    /**
     * Update user's profile.
     */
    fun updateProfile(id: String, user: User): User
}