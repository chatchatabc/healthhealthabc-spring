package com.chatchatabc.patient.application.rest

import com.chatchatabc.api.domain.dto.user.UserRegistrationDTO
import com.chatchatabc.api.domain.service.UserService
import com.chatchatabc.patient.application.dto.ErrorContent
import com.chatchatabc.patient.application.dto.auth.AuthRegisterResponse
import org.apache.dubbo.config.annotation.DubboReference
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    @DubboReference
    private val userService: UserService
) {

    @PostMapping("/login")
    fun login() {
    }

    @PostMapping("/register")
    fun register(@RequestBody registerData: UserRegistrationDTO): ResponseEntity<AuthRegisterResponse> {
        return try {
            println("=====================================")
            println(registerData)
            val registeredUser = userService.register(registerData, "ROLE_PATIENT")
            ResponseEntity.ok(AuthRegisterResponse(registeredUser, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.ok(AuthRegisterResponse(null, ErrorContent("Registration Error", e.message)))
        }
    }
}