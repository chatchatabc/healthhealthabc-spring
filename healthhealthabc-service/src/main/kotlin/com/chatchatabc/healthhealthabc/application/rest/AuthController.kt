//package com.chatchatabc.healthhealthabc.application.rest
//
//import com.chatchatabc.api.application.dto.user.UserDTO
//import com.chatchatabc.api.domain.dto.user.UserRegistrationDTO
//import com.chatchatabc.api.domain.service.UserService
//import com.chatchatabc.healthhealthabc.application.dto.ErrorContent
//import com.chatchatabc.healthhealthabc.application.dto.auth.*
//import com.chatchatabc.healthhealthabc.domain.model.User
//import com.chatchatabc.healthhealthabc.domain.repository.UserRepository
//import com.chatchatabc.healthhealthabc.domain.service.JwtService
//
//import com.chatchatabc.healthhealthabc.domain.service.log.user.LoginLogService
//import jakarta.servlet.http.HttpServletRequest
//import org.hibernate.exception.ConstraintViolationException
//import org.modelmapper.ModelMapper
//import org.springframework.dao.DataIntegrityViolationException
//import org.springframework.http.HttpHeaders
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.web.bind.annotation.*
//import java.sql.SQLIntegrityConstraintViolationException
//import java.util.*
//
//@RestController
//@RequestMapping("/api/auth")
//class AuthController(
//    private val userService: UserService,
//    private val userRepository: UserRepository,
//    private val jwtService: JwtService,
//    private val authenticationManager: AuthenticationManager,
//    private val loginLogService: LoginLogService
//) {
//    val modelMapper = ModelMapper()
//
//    /**
//     * Test endpoint.
//     */
//    @GetMapping("/hello")
//    fun hello(): ResponseEntity<String> {
//        return ResponseEntity.ok("Hello, World!")
//    }
//
//

//
//    /**
//     * Confirm a change email request.
//     */
//    @GetMapping("/confirm-change-email/{emailConfirmationId}")
//    fun confirmChangeEmail(@PathVariable emailConfirmationId: String): ResponseEntity<EmailConfirmationResponse> {
//        return try {
//            val user: UserDTO = userService.confirmEmailChange(emailConfirmationId)
//            val emailConfirmationResponse = EmailConfirmationResponse(user, null)
//            ResponseEntity.status(HttpStatus.OK).body(emailConfirmationResponse)
//        } catch (e: Exception) {
//            val errorContent = ErrorContent("Email Confirmation Error", e.message)
//            val emailConfirmationResponse = EmailConfirmationResponse(null, errorContent)
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emailConfirmationResponse)
//        }
//    }
//
//

//
//    /**
//     * Create recovery code and send to email to reset password.
//     */
//    @PostMapping("/forgot-password")
//    fun forgotPassword(@RequestBody user: User): ResponseEntity<ForgotPasswordResponse> {
//        return try {
//            userService.forgotPassword(user.email)
//            val forgotPasswordResponse = ForgotPasswordResponse(null)
//            ResponseEntity.status(HttpStatus.OK).body(forgotPasswordResponse)
//        } catch (e: Exception) {
//            val errorContent = ErrorContent("Forgot Password Error", e.message)
//            val forgotPasswordResponse = ForgotPasswordResponse(errorContent)
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(forgotPasswordResponse)
//        }
//    }
//
//    /**
//     * Reset password using recovery code and email.
//     */
//    @PostMapping("/reset-password")
//    fun resetPassword(@RequestBody resetPasswordRequest: ResetPasswordRequest): ResponseEntity<ResetPasswordResponse> {
//        return try {
//            val user: UserDTO = userService.resetPassword(
//                resetPasswordRequest.email,
//                resetPasswordRequest.password,
//                resetPasswordRequest.recoveryCode
//            )
//            val resetPasswordResponse = ResetPasswordResponse(user.email, user.username, null)
//            ResponseEntity.status(HttpStatus.OK).body(resetPasswordResponse)
//        } catch (e: Exception) {
//            val errorContent = ErrorContent("Reset Password Error", e.message)
//            val resetPasswordResponse = ResetPasswordResponse(null, null, errorContent)
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resetPasswordResponse)
//        }
//    }
//
//    /**
//     * Check if email is taken by another user.
//     */
//    @PostMapping("/check-email")
//    fun checkEmail(@RequestBody user: User): ResponseEntity<CheckEmailResponse> {
//        return try {
//            val queriedUser = userRepository.findByEmail(user.email)
//            if (queriedUser.isPresent) {
//                throw Exception("Email already taken")
//            }
//            val checkEmailResponse = CheckEmailResponse(false, null)
//            ResponseEntity.status(HttpStatus.OK).body(checkEmailResponse)
//        } catch (e: Exception) {
//            val errorContent = ErrorContent("Check Email Error", e.message)
//            val checkEmailResponse = CheckEmailResponse(true, errorContent)
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkEmailResponse)
//        }
//    }
//
//    /**
//     * Check if username is taken by another user.
//     */
//    @PostMapping("/check-username")
//    fun checkUsername(@RequestBody user: User): ResponseEntity<CheckUsernameResponse> {
//        return try {
//            val queriedUser = userRepository.findByUsername(user.username!!)
//            if (queriedUser.isPresent) {
//                throw Exception("Username already taken")
//            }
//            val checkUsernameResponse = CheckUsernameResponse(false, null)
//            ResponseEntity.status(HttpStatus.OK).body(checkUsernameResponse)
//        } catch (e: Exception) {
//            val errorContent = ErrorContent("Check Username Error", e.message)
//            val checkUsernameResponse = CheckUsernameResponse(true, errorContent)
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkUsernameResponse)
//        }
//    }
//}