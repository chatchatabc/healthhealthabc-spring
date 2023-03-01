package com.chatchatabc.healthhealthabc.impl.domain.event

import com.chatchatabc.healthhealthabc.domain.event.user.UserCreatedEvent
import com.chatchatabc.healthhealthabc.domain.event.user.UserEventProcessor
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
        val emailConfirmationId = event.user.emailConfirmationId

        // Run Quartz job to send email confirmation
        if (email != null && username != null && emailConfirmationId != null) {
            userSchedule.onRegisterSendEmailConfirmation(email, username, emailConfirmationId)
        }
        else {
            throw Exception("User email, username, or email confirmation id is null.")
        }
    }
}