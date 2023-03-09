package com.chatchatabc.admin.infra.config.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtRequestFilter () : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(JwtRequestFilter::class.java)

    /**
     * Filter the request and validate the token
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // TODO: Implement
        filterChain.doFilter(request, response)
    }
}