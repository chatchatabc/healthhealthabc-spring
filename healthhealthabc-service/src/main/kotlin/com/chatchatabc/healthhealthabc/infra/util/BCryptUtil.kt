package com.chatchatabc.healthhealthabc.infra.util

import org.mindrot.jbcrypt.BCrypt

class BCryptUtil {

    companion object {
        /**
         * Hashes a password using BCrypt.
         */
        fun hashPassword(password: String): String {
            return BCrypt.hashpw(password, BCrypt.gensalt())
        }

        /**
         * Checks if a password matches a hashed password.
         */
        fun checkPassword(password: String, hashedPassword: String): Boolean {
            return BCrypt.checkpw(password, hashedPassword)
        }
    }
}