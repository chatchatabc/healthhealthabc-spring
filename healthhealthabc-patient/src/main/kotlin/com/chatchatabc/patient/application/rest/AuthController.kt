package com.chatchatabc.patient.application.rest

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.api.application.dto.auth.AuthLoginRequest
import com.chatchatabc.api.application.dto.auth.AuthLoginResponse
import com.chatchatabc.api.application.dto.auth.AuthRegisterRequest
import com.chatchatabc.api.application.dto.auth.AuthRegisterResponse
import com.chatchatabc.api.domain.service.UserService
import jakarta.servlet.http.HttpServletRequest
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

    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        userService.test("Hello, World!")
        return ResponseEntity.ok("Hello, World!")
    }

    /**
     * Login a user
     */
    @PostMapping("/login")
    fun login(
        request: HttpServletRequest,
        @RequestBody loginData: AuthLoginRequest
    ): ResponseEntity<AuthLoginResponse> {
        return try {
            // TODO: Create login logic
            ResponseEntity.ok(AuthLoginResponse(null, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthLoginResponse(null, ErrorContent("Login Error", e.message)))
        }

    }

    /**
     * Register a new user
     */
    @PostMapping("/register")
    fun register(@RequestBody registerData: AuthRegisterRequest): ResponseEntity<AuthRegisterResponse> {
        return try {
            val registeredUser = userService.register(registerData, "ROLE_PATIENT")
            ResponseEntity.ok(AuthRegisterResponse(registeredUser, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthRegisterResponse(null, ErrorContent("Registration Error", e.message)))
        }
    }
}