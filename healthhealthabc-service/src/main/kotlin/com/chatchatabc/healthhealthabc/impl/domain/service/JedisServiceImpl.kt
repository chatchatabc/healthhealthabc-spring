package com.chatchatabc.healthhealthabc.impl.domain.service

import com.chatchatabc.healthhealthabc.domain.service.JedisService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import redis.clients.jedis.Jedis

@Service
class JedisServiceImpl (
    @Value("\${spring.data.redis.host}")
    private var host: String,

    @Value("\${spring.data.redis.port}")
    private var port: String
) : JedisService {
    /**
     * Set a key-value pair to Redis.
     */
    override fun set(key: String, value: String) {
        val jedis = Jedis(host, port.toInt())
        jedis.set(key, value)
        jedis.close()
    }

    /**
     * Set a key-value pair to Redis with a TTL.
     */
    override fun setTTL(key: String, value: String, ttl: Long) {
        val jedis = Jedis(host, port.toInt())
        jedis.setex(key, ttl, value)
        jedis.close()
    }

    /**
     * Get a value from Redis.
     */
    override fun get(key: String): String? {
        val jedis = Jedis(host, port.toInt())
        val value = jedis.get(key)
        jedis.close()
        return value
    }

    /**
     * Delete a key-value pair from Redis.
     */
    override fun delete(key: String) {
        val jedis = Jedis(host, port.toInt())
        jedis.del(key)
        jedis.close()
    }
}