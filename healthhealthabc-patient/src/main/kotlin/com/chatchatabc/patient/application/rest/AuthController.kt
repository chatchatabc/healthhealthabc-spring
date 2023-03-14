package com.chatchatabc.patient.application.rest

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.api.application.dto.auth.*
import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.api.domain.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.apache.dubbo.config.annotation.DubboReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    @DubboReference
    private val userService: UserService,
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
        var user: UserDTO? = null
        return try {
            val authLoginResponse = userService.login(loginData, request.remoteAddr)
            user = authLoginResponse.user
            val headers = HttpHeaders()
            headers.set("X-Access-Token", authLoginResponse.token)
            ResponseEntity.ok().headers(headers).body(AuthLoginResponse(user, null))
        } catch (e: Exception) {
            // Save to log in logs with failed login
//            // TODO: Transfer logic to JwtService?
//            if (queriedUser != null && queriedUser.isPresent) {
//                loginLogService.createLog(queriedUser.get(), false, queriedUser.get().email, request.remoteAddr)
//            }
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

    /**
     * Confirm a change email request
     */
    @GetMapping("/confirm-change-email/{emailConfirmationId}")
    fun confirmChangeEmail(@PathVariable emailConfirmationId: String): ResponseEntity<AuthEmailConfirmationResponse> {
        return try {
            val user: UserDTO = userService.confirmEmailChange(emailConfirmationId)
            ResponseEntity.status(HttpStatus.OK).body(AuthEmailConfirmationResponse(user, null))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthEmailConfirmationResponse(null, ErrorContent("Confirmation Error", e.message)))
        }
    }

    /**
     * Create recovery code and send to email to reset password
     */
    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody user: UserDTO): ResponseEntity<AuthForgotPasswordResponse> {
        return try {
            userService.forgotPassword(user.email)
            ResponseEntity.ok(AuthForgotPasswordResponse(null))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthForgotPasswordResponse(ErrorContent("Forgot Password Error", e.message)))
        }
    }

    /**
     * Reset password using recovery code and email
     */
    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody resetPasswordData: AuthResetPasswordRequest): ResponseEntity<AuthResetPasswordResponse> {
        return try {
            val user = userService.resetPassword(
                resetPasswordData.email,
                resetPasswordData.recoveryCode,
                resetPasswordData.password
            )
            ResponseEntity.ok(AuthResetPasswordResponse(user.email, user.username, null))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthResetPasswordResponse(null, null, ErrorContent("Reset Password Error", e.message)))
        }
    }

    /**
     * Check if email is taken by another user
     */
    @PostMapping("/check-email")
    fun checkEmail(@RequestBody user: UserDTO): ResponseEntity<AuthEmailCheckResponse> {
        return try {
            val isTaken = userService.checkEmail(user.email)
            ResponseEntity.ok(AuthEmailCheckResponse(isTaken, null))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthEmailCheckResponse(true, ErrorContent("Check Email Error", e.message)))
        }
    }

    /**
     * Check if username is taken by another user
     */
    @PostMapping("/check-username")
    fun checkUsername(@RequestBody user: UserDTO): ResponseEntity<AuthUsernameCheckResponse> {
        return try {
            val isTaken = userService.checkUsername(user.username)
            ResponseEntity.ok(AuthUsernameCheckResponse(isTaken, null))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthUsernameCheckResponse(true, ErrorContent("Check Username Error", e.message)))
        }
    }
}