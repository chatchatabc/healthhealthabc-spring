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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController(
    @DubboReference
    private val userService: UserService,
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
}