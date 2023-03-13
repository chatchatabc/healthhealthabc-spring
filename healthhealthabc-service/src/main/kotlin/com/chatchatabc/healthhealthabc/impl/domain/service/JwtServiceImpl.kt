package com.chatchatabc.healthhealthabc.impl.domain.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.chatchatabc.api.domain.service.JwtService
import com.chatchatabc.healthhealthabc.domain.service.log.user.LoginLogService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class JwtServiceImpl(
    @Value("\${jwt.secret}")
    private var secret: String,

    @Value("\${jwt.expiration}")
    private var expiration: String,

    private val loginLogService: LoginLogService
) : JwtService {
    private var hmac512: Algorithm = Algorithm.HMAC512(secret)
    private var verifier: JWTVerifier = JWT.require(hmac512).build()

    /**
     * Generate a token for the given user
     */
    override fun generateToken(username: String, ipAddress: String): String {
        // Save to log in logs with successful login
//        val sessionId = loginLogService.createLog(user, true, user.email, ipAddress)
        // TODO: Search for user here and use that

        return JWT.create()
//            .withSubject(user.id)
//            .withClaim("sessionId", sessionId)
            .withExpiresAt(Date.from(Instant.now().plusSeconds(expiration.toLong())))
            .sign(hmac512)
    }

    /**
     * Validate the given token
     */
    override fun validateTokenAndGetId(token: String): String {
        return try {
            this.verifier.verify(token).subject
        } catch (e: Exception) {
            throw Exception("Invalid token")
        }
    }

}