package com.chatchatabc.healthhealthabc.application.rest

import com.chatchatabc.healthhealthabc.application.dto.ErrorContent
import com.chatchatabc.healthhealthabc.application.dto.auth.LoginRequest
import com.chatchatabc.healthhealthabc.application.dto.auth.LoginResponse
import com.chatchatabc.healthhealthabc.application.dto.auth.RegisterResponse
import com.chatchatabc.healthhealthabc.domain.model.Role
import com.chatchatabc.healthhealthabc.domain.model.User
import com.chatchatabc.healthhealthabc.domain.repository.UserRepository
import com.chatchatabc.healthhealthabc.domain.service.JwtService
import com.chatchatabc.healthhealthabc.domain.service.UserService
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
    private val authenticationManager: AuthenticationManager
) {

    /**
     * Test endpoint.
     */
    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, World!")
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )
            val queriedUser: Optional<User> = userRepository.findByUsername(loginRequest.username)

            if (queriedUser.isEmpty) {
                throw Exception("User not found")
            }
            val token: String = jwtService.generateToken(queriedUser.get())
            val role: Role = queriedUser.get().roles.elementAt(0)
            val loginResponse: LoginResponse? = queriedUser.get().username?.let {
                queriedUser.get().email?.let { it1 -> LoginResponse(it, it1, role.name) }
            }
            val headers = HttpHeaders()
            headers.set("X-Access-Token", token)
            return ResponseEntity.ok().headers(headers).body(loginResponse)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
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

    fun extractColumnFromMessage(message: String?): String? {
        val pattern = "entry '(.+)' for key".toRegex()
        val matchResult = pattern.find(message ?: "")
        return matchResult?.groups?.get(1)?.value
    }
}