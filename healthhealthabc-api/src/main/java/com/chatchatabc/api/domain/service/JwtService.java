package com.chatchatabc.api.domain.service;

public interface JwtService {
    /**
     * Generate a token for the given user
     */
    String generateToken(String id, String ipAddress);

    /**
     * Validate a token
     */
    String validateTokenAndGetId(String token);
}
