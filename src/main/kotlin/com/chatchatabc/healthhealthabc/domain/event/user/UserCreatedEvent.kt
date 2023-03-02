package com.chatchatabc.healthhealthabc.domain.event.user

import com.chatchatabc.healthhealthabc.domain.model.User
import org.springframework.context.ApplicationEvent

open class UserCreatedEvent(open val user: User, open val emailConfirmationId: String, source: Any) :
    ApplicationEvent(source) {
    override fun getSource(): Any = super.getSource()
}