package com.chatchatabc.doctor.infra.config.security.filter

import com.chatchatabc.doctor.domain.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtRequestFilter (
    private val jwtService: JwtService
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
        // TODO: Implement filter
        filterChain.doFilter(request, response)
    }

}