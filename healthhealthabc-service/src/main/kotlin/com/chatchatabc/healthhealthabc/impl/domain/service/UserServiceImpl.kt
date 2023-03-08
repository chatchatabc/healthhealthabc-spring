package com.chatchatabc.healthhealthabc.impl.domain.service

import com.chatchatabc.healthhealthabc.domain.event.user.UserChangeEmailEvent
import com.chatchatabc.healthhealthabc.domain.event.user.UserChangePasswordEvent
import com.chatchatabc.healthhealthabc.domain.event.user.UserCreatedEvent
import com.chatchatabc.healthhealthabc.domain.event.user.UserForgotPasswordEvent
import com.chatchatabc.healthhealthabc.domain.model.User
import com.chatchatabc.healthhealthabc.domain.repository.RoleRepository
import com.chatchatabc.healthhealthabc.domain.repository.UserRepository
import com.chatchatabc.healthhealthabc.domain.service.JedisService
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
    private val eventPublisher: ApplicationEventPublisher,
    private val jedisService: JedisService,

    @Value("\${user.recoverycode.expiration}")
    private var recoveryCodeExpiration: Long
) : UserService {

    // Get admin.email value from application.properties
    @Value("\${admin.email}")
    private val adminEmail: String? = null

    override fun register(user: User, roleName: String): User {

        // Generate UUID for Confirmation ID
        val confirmationId = UUID.randomUUID().toString()
        // Save confirmationId to Redis
        jedisService.set("email_confirmation_${confirmationId}", user.email)

        user.apply {
            // Encrypt password
            password = passwordEncoder.encode(password)
            // Add role of user
            roles.add(roleRepository.findRoleByName(roleName))
        }.let {
            // Send Email Confirmation
            eventPublisher.publishEvent(UserCreatedEvent(user, confirmationId, this))
            // Save user to database
            return userRepository.save(it)
        }
    }

    /**
     * Confirm a user's registration.
     */
    override fun confirmRegistration(emailConfirmationId: String): User {
        // Get email from Redis
        val email = jedisService.get("email_confirmation_${emailConfirmationId}") ?: throw Exception("Email not found")
        val user: Optional<User> = userRepository.findByEmail(email)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        if (user.get().emailConfirmedAt != null) {
            throw Exception("User already confirmed")
        }
        user.get().apply {
            emailConfirmedAt = Instant.now()
        }.let {
            // Delete key value pair from Redis since it is already confirmed
            jedisService.delete("email_confirmation_${emailConfirmationId}")
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
        // Save recovery code to Redis
        jedisService.setTTL("password_recovery_${email}", recoveryCode, recoveryCodeExpiration)
        eventPublisher.publishEvent(UserForgotPasswordEvent(user.get(), recoveryCode, this))
        return user.get()
    }

    /**
     * Reset user's password.
     */
    override fun resetPassword(email: String, password: String, recoveryCode: String): User {
        val user: Optional<User> = userRepository.findByEmail(email)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        // Get recovery code from Redis
        val redisRecoveryCode = jedisService.get("password_recovery_${email}")
        if (redisRecoveryCode != recoveryCode) {
            throw Exception("Recovery code is incorrect")
        }
        user.get().apply {
            this.password = passwordEncoder.encode(password)
        }.let {
            // Jedis delete key value pair
            jedisService.delete("password_recovery_${email}")
            return userRepository.save(it)
        }
    }

    /**
     * Change user's password.
     */
    override fun changePassword(id: String, oldPassword: String, newPassword: String) {
        val user: Optional<User> = userRepository.findById(id)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        // Check if old password is correct
        if (!passwordEncoder.matches(oldPassword, user.get().password)) {
            throw Exception("Old password is incorrect")
        }
        // Change password
        user.get().apply {
            this.password = passwordEncoder.encode(newPassword)
        }.let {
            // Publish event to send email
            eventPublisher.publishEvent(UserChangePasswordEvent(user.get().email, user.get().username, this))
            userRepository.save(it)
        }
    }

    /**
     * Update user's profile.
     */
    override fun updateProfile(id: String, user: User): User {
        val queriedUser: Optional<User> = userRepository.findById(id)
        if (queriedUser.isEmpty) {
            throw Exception("User not found")
        }
        val innerQueriedUser = queriedUser.get()
        // Update user's profile
        // Username
        innerQueriedUser.apply {
            if (user.username != null) {
                setUsername(user.username!!)
            }
            // TODO: Add more if more fields are added
        }.let {
            return userRepository.save(it)
        }
    }

    /**
     * Change user's email.
     */
    override fun changeEmail(id: String, newEmail: String): User {
        val user: Optional<User> = userRepository.findById(id)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        // Generate UUID for Confirmation ID
        val confirmationId = UUID.randomUUID().toString()
        // Save confirmationId to Redis
        jedisService.set("email_change_original_${confirmationId}", user.get().email)
        jedisService.set("email_change_new_${confirmationId}", newEmail)
        // Publish event to send email
        eventPublisher.publishEvent(UserChangeEmailEvent(newEmail, user.get().username, confirmationId, this))
        return user.get()
    }

    /**
     * Confirm user's email change.
     */
    override fun confirmEmailChange(emailConfirmationId: String): User {
        // Get original and new email from Redis
        val originalEmail = jedisService.get("email_change_original_${emailConfirmationId}") ?: throw Exception("Email not found")
        val newEmail = jedisService.get("email_change_new_${emailConfirmationId}") ?: throw Exception("Email not found")
        val user: Optional<User> = userRepository.findByEmail(originalEmail)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        user.get().apply {
            email = newEmail
        }.let {
            // Delete key value pair from Redis since it is already confirmed
            jedisService.delete("email_change_original_${emailConfirmationId}")
            jedisService.delete("email_change_new_${emailConfirmationId}")
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