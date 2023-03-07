package com.chatchatabc.healthhealthabc.impl.domain.event

import com.chatchatabc.healthhealthabc.domain.event.user.UserChangePasswordEvent
import com.chatchatabc.healthhealthabc.domain.event.user.UserCreatedEvent
import com.chatchatabc.healthhealthabc.domain.event.user.UserEventProcessor
import com.chatchatabc.healthhealthabc.domain.event.user.UserForgotPasswordEvent
import com.chatchatabc.healthhealthabc.infra.schedule.user.UserSchedule
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class UserEventProcessorImpl(private val userSchedule: UserSchedule) : UserEventProcessor {
    /**
     * Handle user created event.
     */
    @Async
    @EventListener
    override fun handleUserCreatedEvent(event: UserCreatedEvent) {
        val email = event.user.email
        val username = event.user.username
        val emailConfirmationId = event.emailConfirmationId

        // Run Quartz job to send email confirmation
        if (email != null && username != null) {
            userSchedule.onRegisterSendEmailConfirmation(email, username, emailConfirmationId)
        } else {
            throw Exception("User email, username, or email confirmation id is null.")
        }
    }

    /**
     * Handle user forgot password event.
     */
    @Async
    @EventListener
    override fun handleUserForgotPasswordEvent(event: UserForgotPasswordEvent) {
        val email = event.user.email
        val username = event.user.username
        val recoveryCode = event.recoveryCode

        // Run Quartz job to send email confirmation
        if (email != null && username != null) {
            userSchedule.onForgotPasswordSendEmail(email, username, recoveryCode)
        } else {
            throw Exception("User email, username, or recovery code is null.")
        }
    }

    /**
     * Handle user change password event.
     */
    @Async
    @EventListener
    override fun handleUserChangePasswordEvent(event: UserChangePasswordEvent) {
        val email = event.email
        val username = event.username

        if (email != null && username != null) {
            // Run Quartz job to send change password notification
            userSchedule.onChangePasswordSendEmail(email, username)
        } else {
            throw Exception("User email or username is null.")
        }
    }
}