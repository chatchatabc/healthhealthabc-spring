package com.chatchatabc.healthhealthabc.application.rest

import com.chatchatabc.healthhealthabc.application.dto.ErrorContent
import com.chatchatabc.healthhealthabc.application.dto.user.UserChangePasswordRequest
import com.chatchatabc.healthhealthabc.application.dto.user.UserChangePasswordResponse
import com.chatchatabc.healthhealthabc.application.dto.user.UserDTO
import com.chatchatabc.healthhealthabc.application.dto.user.UserProfileResponse
import com.chatchatabc.healthhealthabc.domain.model.User
import com.chatchatabc.healthhealthabc.domain.repository.UserRepository
import com.chatchatabc.healthhealthabc.domain.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userRepository: UserRepository,
    private val userService: UserService
) {

    /**
     * Get User Profile
     */
    @GetMapping("/profile")
    fun profile(request: HttpServletRequest): ResponseEntity<UserProfileResponse> {
        return try {
            // Get ID from request
            val id = request.getAttribute("userId") as String
            val user: Optional<User> = userRepository.findById(id)
            if (user.isEmpty) {
                throw Exception("User not found")
            }
            // TODO: Improve this DTO
            val userDTO = UserDTO(
                user.get().id,
                user.get().email,
                user.get().username,
                user.get().emailConfirmedAt,
                user.get().createdAt,
                user.get().updatedAt
            )
            val userProfileResponse = UserProfileResponse(userDTO, null)
            ResponseEntity.ok(userProfileResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            val errorContent = ErrorContent("Error", e.message)
            val userProfileResponse = UserProfileResponse(null, errorContent)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userProfileResponse)
        }
    }

    // TODO: Update Profile

    /**
     * Change Password
     */
    @PostMapping("/change-password")
    fun changePassword(
        request: HttpServletRequest,
        @RequestBody userChangePasswordRequest: UserChangePasswordRequest
    ): ResponseEntity<UserChangePasswordResponse> {
        return try {
            // Get ID from request
            val id = request.getAttribute("userId") as String
            userService.changePassword(id, userChangePasswordRequest.oldPassword, userChangePasswordRequest.newPassword)
            val userChangePasswordResponse = UserChangePasswordResponse(null)
            ResponseEntity.ok(userChangePasswordResponse)
        } catch (e: Exception) {
            val errorContent = ErrorContent("Error", e.message)
            val userChangePasswordResponse = UserChangePasswordResponse(errorContent)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userChangePasswordResponse)
        }
    }
}