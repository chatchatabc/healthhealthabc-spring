package com.chatchatabc.patient.infra.config.security.filter

import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.api.domain.service.JwtService
import com.chatchatabc.api.domain.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.dubbo.config.annotation.DubboReference
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtRequestFilter(
    @DubboReference
    private val jwtService: JwtService,
    @DubboReference
    private val userService: UserService
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtRequestFilter::class.java)

    /**
     * Filter the request and validate the token
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Get X-Access-Token from request header
        val header: String? = request.getHeader("X-Access-Token")

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            logRequest(request, response)
            return
        }

        val token: String = header.substring(7)
        val userId: String = jwtService.validateTokenAndGetId(token)

        // Get the user from the database
        val user: Optional<UserDTO> = userService.getUserById(userId)

        // If the user is not found
        if (user.isEmpty) {
            filterChain.doFilter(request, response)
            logRequest(request, response)
            return
        }
//        val authorities: MutableCollection<out GrantedAuthority> =
//            user.get().roles.stream().map { role -> role as GrantedAuthority }.toList().toMutableList()

        // Set user id on request attribute
        request.setAttribute("userId", userId)

        val authentication = UsernamePasswordAuthenticationToken(
            user.get(),
            null,
//            authorities
            null
        )
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication

        // Continue flow with authenticated user
        filterChain.doFilter(request, response)
        logRequest(request, response, userId)
    }

    /**
     * Log the request path and the response code
     */
    fun logRequest(request: HttpServletRequest, response: HttpServletResponse) {
        // Log the path of the request
        log.info("Request path: ${request.method} ${request.requestURL} from ${request.remoteAddr} with code ${response.status}")
    }

    /**
     * Log the request path and the response code with user id
     */
    fun logRequest(request: HttpServletRequest, response: HttpServletResponse, userId: String) {
        // Log the path of the request
        log.info("Request path: ${request.method} ${request.requestURL} User ID $userId from ${request.remoteAddr} with code ${response.status}")
    }

}