package com.chatchatabc.healthhealthabc.domain.repository

import com.chatchatabc.healthhealthabc.domain.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, String> {

    /**
     * Find roles by name in.
     */
    fun findRolesByNameIn(roleNames: List<String>): List<Role>

    /**
     * Find role by name.
     */
    fun findRoleByName(name: String): Role
}