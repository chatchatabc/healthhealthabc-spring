package com.chatchatabc.healthhealthabc.domain.event.user

interface UserEventProcessor {

    /**
     * Handle user created event.
     */
    fun handleUserCreatedEvent(event: UserCreatedEvent)

    /**
     * Handle user forgot password event.
     */
    fun handleUserForgotPasswordEvent(event: UserForgotPasswordEvent)
}