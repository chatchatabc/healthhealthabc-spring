package com.chatchatabc.healthhealthabc.impl.domain.service

import com.chatchatabc.healthhealthabc.domain.event.user.UserCreatedEvent
import com.chatchatabc.healthhealthabc.domain.event.user.UserForgotPasswordEvent
import com.chatchatabc.healthhealthabc.domain.model.User
import com.chatchatabc.healthhealthabc.domain.repository.RoleRepository
import com.chatchatabc.healthhealthabc.domain.repository.UserRepository
import com.chatchatabc.healthhealthabc.domain.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class UserServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val eventPublisher: ApplicationEventPublisher
) : UserService {

    // Get admin.email value from application.properties
    @Value("\${admin.email}")
    private val adminEmail: String? = null

    override fun register(user: User, roleName: String): User {

        // Generate UUID for Confirmation ID
        val confirmationId = UUID.randomUUID().toString()

        user.apply {
            // Encrypt password
            password = passwordEncoder.encode(password)
            // Add role of user
            roles.add(roleRepository.findRoleByName(roleName))
            // Set email confirmation ID
            emailConfirmationId = confirmationId
        }.let {
            // Send Email Confirmation
            eventPublisher.publishEvent(UserCreatedEvent(user, this))
            // Save user to database
            return userRepository.save(it)
        }
    }

    /**
     * Confirm a user's registration.
     */
    override fun confirmRegistration(emailConfirmationId: String): User {
        val user: Optional<User> = userRepository.findByEmailConfirmationId(emailConfirmationId)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        if (user.get().emailConfirmedAt != null) {
            throw Exception("User already confirmed")
        }
        user.get().apply {
            emailConfirmedAt = Instant.now()
        }.let {
            return userRepository.save(it)
        }
    }

    /**
     * Generate recovery code for user to use.
     */
    override fun forgotPassword(email: String): User {
        val user: Optional<User> = userRepository.findByEmail(email)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        if (user.get().emailConfirmedAt == null) {
            throw Exception("User not confirmed")
        }
        // Generate recovery code 6-digit
        val recoveryCode = String.format("%06d", (Math.random() * 1000000).toInt())
        user.get().apply {
            this.recoveryCode = recoveryCode
        }.let {
            eventPublisher.publishEvent(UserForgotPasswordEvent(user.get(), this))
            return userRepository.save(it)
        }
    }

    /**
     * Reset user's password.
     */
    override fun resetPassword(email: String, password: String, recoveryCode: String): User {
        val user: Optional<User> = userRepository.findByEmail(email)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        if (user.get().recoveryCode != recoveryCode) {
            throw Exception("Recovery code is incorrect")
        }
        user.get().apply {
            this.password = passwordEncoder.encode(password)
            this.recoveryCode = null
        }.let {
            return userRepository.save(it)
        }
    }

    /**
     * Load a user by their username.
     */
    override fun loadUserByUsername(username: String): UserDetails {
        val user: Optional<User> = userRepository.findByUsername(username)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        return org.springframework.security.core.userdetails.User(
            user.get().username,
            user.get().password,
            user.get().isEnabled,
            user.get().isAccountNonExpired,
            user.get().isCredentialsNonExpired,
            user.get().isAccountNonLocked,
            user.get().authorities
        )
    }
}