package com.chatchatabc.healthhealthabc.application.rest

import com.chatchatabc.healthhealthabc.application.dto.ErrorContent
import com.chatchatabc.healthhealthabc.application.dto.auth.*
import com.chatchatabc.healthhealthabc.domain.model.Role
import com.chatchatabc.healthhealthabc.domain.model.User
import com.chatchatabc.healthhealthabc.domain.repository.UserRepository
import com.chatchatabc.healthhealthabc.domain.service.JwtService
import com.chatchatabc.healthhealthabc.domain.service.UserService
import com.chatchatabc.healthhealthabc.domain.service.log.user.LoginLogService
import jakarta.servlet.http.HttpServletRequest
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.sql.SQLIntegrityConstraintViolationException
import java.util.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val loginLogService: LoginLogService
) {

    /**
     * Test endpoint.
     */
    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, World!")
    }

    @PostMapping("/login")
    fun login(request: HttpServletRequest, @RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        var queriedUser: Optional<User>? = null
        try {
            queriedUser = userRepository.findByUsername(loginRequest.username)
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )

            if (queriedUser.isEmpty) {
                throw Exception("User not found")
            }
            // TODO: Uncomment after email confirmation is implemented by Bon
//            if (queriedUser.get().emailConfirmedAt == null) {
//                throw Exception("Email not confirmed")
//            }
            val ipAddress: String = request.remoteAddr
            val token: String = jwtService.generateToken(queriedUser.get(), ipAddress)
            val role: Role = queriedUser.get().roles.elementAt(0)
            val loginResponse: LoginResponse? = queriedUser.get().username?.let {
                queriedUser.get().email?.let { it1 -> LoginResponse(it, it1, role.name, null) }
            }
            val headers = HttpHeaders()
            headers.set("X-Access-Token", token)

            return ResponseEntity.ok().headers(headers).body(loginResponse)
        } catch (e: Exception) {
            // Save to log in logs with failed login
            // TODO: Transfer logic to JwtService?
            if (queriedUser != null && queriedUser.isPresent) {
                loginLogService.createLog(queriedUser.get(), false, queriedUser.get().email!!, request.remoteAddr)
            }
            // Get error message
            val errorContent = ErrorContent("Login Error", e.message)
            val loginResponse = LoginResponse(null, null, null, errorContent)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse)
        }
    }

    /**
     * Register a new user either doctor or patient.
     */
    @PostMapping("/register/{roleParams}")
    fun register(@RequestBody user: User, @PathVariable roleParams: String): ResponseEntity<RegisterResponse> {
        try {
            var roleName = "ROLE_PATIENT"
            if (roleParams == "doctor") {
                roleName = "ROLE_DOCTOR"
            }
            val registeredUser: User = userService.register(user, roleName)
            val registerResponse = RegisterResponse(registeredUser.username, registeredUser.email, null)
            return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse)
        } catch (e: DataIntegrityViolationException) {
            val cause = e.cause
            var column: String? = null
            var message: String? = null
            var errorContent: ErrorContent? = null
            if (cause is ConstraintViolationException) {
                val sqlException = cause.cause as? SQLIntegrityConstraintViolationException
                if (sqlException != null) {
                    val sqlMessage = sqlException.message
                    column = extractColumnFromMessage(sqlMessage)
                    message = "is already taken"
                    errorContent = column?.let { ErrorContent(it, message) }
                }
            }
            val registerResponse = RegisterResponse(null, null, errorContent)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponse)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
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
     * Confirm email using email confirmation id.
     */
    @GetMapping("/confirm-email/{emailConfirmationId}")
    fun confirmEmail(@PathVariable emailConfirmationId: String): ResponseEntity<EmailConfirmationResponse> {
        return try {
            val user: User = userService.confirmRegistration(emailConfirmationId)
            val emailConfirmationResponse = EmailConfirmationResponse(user.username, user.email, null)
            ResponseEntity.status(HttpStatus.OK).body(emailConfirmationResponse)
        } catch (e: Exception) {
            val errorContent = ErrorContent("Email Confirmation Error", e.message)
            val emailConfirmationResponse = EmailConfirmationResponse(null, null, errorContent)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emailConfirmationResponse)
        }
    }

    /**
     * Create error code and send to email to reset password.
     */
    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody user: User): ResponseEntity<ForgotPasswordResponse> {
        return try {
            userService.forgotPassword(user.email!!)
            val forgotPasswordResponse = ForgotPasswordResponse(null)
            ResponseEntity.status(HttpStatus.OK).body(forgotPasswordResponse)
        } catch (e: Exception) {
            val errorContent = ErrorContent("Forgot Password Error", e.message)
            val forgotPasswordResponse = ForgotPasswordResponse(errorContent)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(forgotPasswordResponse)
        }
    }

    /**
     * Reset password using recovery code and email.
     */
    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody resetPasswordRequest: ResetPasswordRequest): ResponseEntity<ResetPasswordResponse> {
        return try {
            val user: User = userService.resetPassword(
                resetPasswordRequest.email,
                resetPasswordRequest.password,
                resetPasswordRequest.recoveryCode
            )
            val resetPasswordResponse = ResetPasswordResponse(user.email, user.username, null)
            ResponseEntity.status(HttpStatus.OK).body(resetPasswordResponse)
        } catch (e: Exception) {
            val errorContent = ErrorContent("Reset Password Error", e.message)
            val resetPasswordResponse = ResetPasswordResponse(null, null, errorContent)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resetPasswordResponse)
        }
    }

    /**
     * Check if email is taken by another user.
     */
    @PostMapping("/check-email")
    fun checkEmail(@RequestBody user: User): ResponseEntity<CheckEmailResponse> {
        return try {
            val queriedUser = userRepository.findByEmail(user.email!!)
            if (queriedUser.isPresent) {
                throw Exception("Email already taken")
            }
            val checkEmailResponse = CheckEmailResponse(false, null)
            ResponseEntity.status(HttpStatus.OK).body(checkEmailResponse)
        } catch (e: Exception) {
            val errorContent = ErrorContent("Check Email Error", e.message)
            val checkEmailResponse = CheckEmailResponse(true, errorContent)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkEmailResponse)
        }
    }

    /**
     * Check if username is taken by another user.
     */
    @PostMapping("/check-username")
    fun checkUsername(@RequestBody user: User): ResponseEntity<CheckUsernameResponse> {
        return try {
            val queriedUser = userRepository.findByUsername(user.username!!)
            if (queriedUser.isPresent) {
                throw Exception("Username already taken")
            }
            val checkUsernameResponse = CheckUsernameResponse(false, null)
            ResponseEntity.status(HttpStatus.OK).body(checkUsernameResponse)
        } catch (e: Exception) {
            val errorContent = ErrorContent("Check Username Error", e.message)
            val checkUsernameResponse = CheckUsernameResponse(true, errorContent)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkUsernameResponse)
        }
    }
}