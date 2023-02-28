package com.chatchatabc.healthhealthabc.application.rest

import com.chatchatabc.healthhealthabc.domain.model.User
import com.chatchatabc.healthhealthabc.domain.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {

    /**
     * Test endpoint.
     */
    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, World!")
    }

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    fun register(@RequestBody user: User): ResponseEntity<User> {
        return try {
            val registeredUser: User = userService.register(user)
            ResponseEntity.status(HttpStatus.CREATED).body(registeredUser)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }
}