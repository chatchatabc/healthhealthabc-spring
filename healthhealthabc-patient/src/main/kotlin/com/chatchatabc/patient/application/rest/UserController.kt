package com.chatchatabc.patient.application.rest

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.api.application.dto.user.UserProfileResponse
import com.chatchatabc.api.domain.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.apache.dubbo.config.annotation.DubboReference
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController(
    @DubboReference
    private val userService: UserService,
//    @DubboReference
//    private val logoutLogService: LogoutLogService
) {

    /**
     * Get User Profile
     */
    @GetMapping("/profile")
    fun profile(request: HttpServletRequest): ResponseEntity<UserProfileResponse> {
        return try {
            // Get ID from request
            val id = request.getAttribute("userId") as String
            val user: UserDTO = userService.getProfile(id)
            ResponseEntity.ok(UserProfileResponse(user, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserProfileResponse(null, ErrorContent("Error", e.message)))
        }
    }

//    /**
//     * Change User Password
//     */
//    @PostMapping("/change-email")
//    fun changeEmail(request: HttpServletRequest, @RequestBody user: User): ResponseEntity<UserChangeEmailResponse> {
//        return try {
//            // Get ID from request
//            val id = request.getAttribute("userId") as String
//            val updatedUser = userService.changeEmail(id, user.email)
//            val userChangeEmailResponse = UserChangeEmailResponse( updatedUser, null)
//            ResponseEntity.ok(userChangeEmailResponse)
//        } catch (e: Exception) {
//            val errorContent = ErrorContent("Error", e.message)
//            val userChangeEmailResponse = UserChangeEmailResponse(null, errorContent)
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userChangeEmailResponse)
//        }
//    }
//
//    /**
//     * Update User Profile
//     */
//    @PutMapping("/profile")
//    fun updateProfile(request: HttpServletRequest, @RequestBody user: UserDTO): ResponseEntity<UserDTO> {
//        return try {
//            // Get ID from request
//            val id = request.getAttribute("userId") as String
//            val updatedUser = userService.updateProfile(id, user)
//            ResponseEntity.ok(updatedUser)
//        } catch (e: Exception) {
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
//        }
//    }

//    /**
//     * Logout user record as log
//     */
//    @PostMapping("/logout")
//    fun logout(request: HttpServletRequest): ResponseEntity<String> {
//        return try {
//            // Get ID from request
//            val id = request.getAttribute("userId") as String
//            logoutLogService.createLog(id, request.remoteAddr)
//            ResponseEntity.ok(null)
//        } catch (e: Exception) {
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("null)
//        }
//    }

    //    /**
//     * Change Password
//     */
//    @PostMapping("/change-password")
//    fun changePassword(
//        request: HttpServletRequest,
//        @RequestBody userChangePasswordRequest: UserChangePasswordRequest
//    ): ResponseEntity<UserChangePasswordResponse> {
//        return try {
//            // Get ID from request
//            val id = request.getAttribute("userId") as String
//            userService.changePassword(
//                id,
//                userChangePasswordRequest.oldPassword,
//                userChangePasswordRequest.newPassword
//            )
//            val userChangePasswordResponse = UserChangePasswordResponse(null)
//            ResponseEntity.ok(userChangePasswordResponse)
//        } catch (e: Exception) {
//            val errorContent = ErrorContent("Error", e.message)
//            val userChangePasswordResponse = UserChangePasswordResponse(errorContent)
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userChangePasswordResponse)
//        }
//    }
}