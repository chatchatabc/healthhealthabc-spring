package com.chatchatabc.patient.application.rest

import com.chatchatabc.api.domain.dto.user.UserRegistrationDTO
import com.chatchatabc.api.domain.service.UserService
import com.chatchatabc.patient.application.dto.ErrorContent
import com.chatchatabc.patient.application.dto.auth.AuthRegisterResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.dubbo.config.annotation.DubboReference
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    @DubboReference
    private val userService: UserService
) {
    val objectMapper = ObjectMapper()

    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        userService.test("Hello, World!")
        return ResponseEntity.ok("Hello, World!")
    }

    @PostMapping("/login")
    fun login() {
    }

    @PostMapping("/register")
    fun register(@RequestBody registerData: UserRegistrationDTO): ResponseEntity<AuthRegisterResponse> {
        return try {
            println(registerData)
            val registeredUser = userService.register(registerData, "ROLE_PATIENT")
            ResponseEntity.ok(AuthRegisterResponse(registeredUser, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthRegisterResponse(null, ErrorContent("Registration Error", e.message)))
        }
    }
}