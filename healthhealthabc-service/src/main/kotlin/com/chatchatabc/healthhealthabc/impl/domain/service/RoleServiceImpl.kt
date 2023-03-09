package com.chatchatabc.healthhealthabc.impl.domain.service

import com.chatchatabc.healthhealthabc.domain.model.Role
import com.chatchatabc.healthhealthabc.domain.repository.RoleRepository
import com.chatchatabc.healthhealthabc.domain.service.RoleService
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(
    var roleRepository: RoleRepository,
) : RoleService {

    /**
     * Create roles.
     */
    override fun createRoles() {
        val roleNames = listOf("ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_PATIENT")
        val roles = roleRepository.findRolesByNameIn(roleNames)
        if (roles.isEmpty()) {
            val adminRole = Role("ROLE_ADMIN")
            val doctorRole = Role("ROLE_DOCTOR")
            val patientRole = Role("ROLE_PATIENT")
            roleRepository.saveAll(listOf(adminRole, doctorRole, patientRole))
        }
    }
}