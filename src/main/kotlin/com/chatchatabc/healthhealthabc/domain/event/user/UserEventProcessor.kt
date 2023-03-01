package com.chatchatabc.healthhealthabc.domain.event.user

interface UserEventProcessor {

    /**
     * Handle user created event.
     */
    fun handleUserCreatedEvent(event: UserCreatedEvent)
}