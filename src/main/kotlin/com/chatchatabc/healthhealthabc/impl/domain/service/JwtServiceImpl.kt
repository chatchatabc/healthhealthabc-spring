package com.chatchatabc.healthhealthabc.impl.domain.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.chatchatabc.healthhealthabc.domain.model.User
import com.chatchatabc.healthhealthabc.domain.service.JwtService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class JwtServiceImpl(
    @Value("\${jwt.secret}")
    private var secret: String,

    @Value("\${jwt.expiration}")
    private var expiration: String
) : JwtService {
    private var hmac512: Algorithm = Algorithm.HMAC512(secret)
    private var verifier: JWTVerifier = JWT.require(hmac512).build()

    /**
     * Generate a token for the given user
     */
    override fun generateToken(user: User): String {
        return JWT.create()
            .withSubject(user.id.toString())
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