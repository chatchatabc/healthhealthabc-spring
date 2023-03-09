package com.chatchatabc.patient.infra.config.security.filter

import com.chatchatabc.patient.domain.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtRequestFilter (
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    /**
     * Filter the request and validate the token
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // TODO: Implement filter
        filterChain.doFilter(request, response)
    }

}