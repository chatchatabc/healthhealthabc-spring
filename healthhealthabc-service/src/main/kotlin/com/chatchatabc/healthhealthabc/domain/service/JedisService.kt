package com.chatchatabc.healthhealthabc.domain.service

import org.springframework.stereotype.Service

@Service
interface JedisService {
    /**
     * Set a key-value pair to Redis.
     */
    fun set(key: String, value: String)

    /**
     * Set a key-value pair to Redis with a TTL.
     */
    fun setTTL(key: String, value: String, ttl: Long)

    /**
     * Get a value from Redis.
     */
    fun get(key: String): String?

    /**
     * Delete a key-value pair from Redis.
     */
    fun delete(key: String)
}