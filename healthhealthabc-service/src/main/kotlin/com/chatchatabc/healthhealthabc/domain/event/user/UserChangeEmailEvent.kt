package com.chatchatabc.healthhealthabc.domain.event.user

import org.springframework.context.ApplicationEvent

open class UserChangeEmailEvent(
    open val email: String?,
    open val username: String?,
    open val emailConfirmationId: String,
    source: Any
) :
    ApplicationEvent(source) {
    override fun getSource(): Any = super.getSource()
}