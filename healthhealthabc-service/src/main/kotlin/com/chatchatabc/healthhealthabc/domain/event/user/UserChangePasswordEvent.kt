package com.chatchatabc.healthhealthabc.domain.event.user

import org.springframework.context.ApplicationEvent

open class UserChangePasswordEvent(open val email: String?, open val username: String?, source: Any) :
    ApplicationEvent(source) {
    override fun getSource(): Any = super.getSource()
}