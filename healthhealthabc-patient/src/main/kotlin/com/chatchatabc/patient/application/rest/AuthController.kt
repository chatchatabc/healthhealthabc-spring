package com.chatchatabc.patient.application.rest

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.api.application.dto.auth.*
import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.api.domain.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.apache.dubbo.config.annotation.DubboReference
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    @DubboReference
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
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
            // TODO: Transfer to a service
            registerData.password = passwordEncoder.encode(registerData.password)
            val registeredUser = userService.register(registerData, "ROLE_PATIENT")
            ResponseEntity.ok(AuthRegisterResponse(registeredUser, null))
        } catch (e: RuntimeException) {
            val column: String? = extractColumnFromMessage(e.message)
            var message: String? = null
            if (column != null) {
                message = "is already taken"
            }
            val errorContent: ErrorContent? = column?.let { ErrorContent(it, message) }
            val registerResponse = AuthRegisterResponse(null, errorContent)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponse)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthRegisterResponse(null, ErrorContent("Registration Error", e.message)))
        }
    }

    /**
     * Extract column name from SQL exception message.
     */
    fun extractColumnFromMessage(message: String?): String? {
        val pattern = "entry '(.+)' for key".toRegex()
        val matchResult = pattern.find(message ?: "")
        return matchResult?.groups?.get(1)?.value
    }

    /**
     * Confirm email using email confirmation id
     */
    @GetMapping("/confirm-email/{emailConfirmationId}")
    fun confirmEmail(@PathVariable emailConfirmationId: String): ResponseEntity<AuthEmailConfirmationResponse> {
        return try {
            val user: UserDTO = userService.confirmRegistration(emailConfirmationId)
            ResponseEntity.status(HttpStatus.OK).body(AuthEmailConfirmationResponse(user, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthEmailConfirmationResponse(null, ErrorContent("Confirmation Error", e.message)))
        }
    }
}