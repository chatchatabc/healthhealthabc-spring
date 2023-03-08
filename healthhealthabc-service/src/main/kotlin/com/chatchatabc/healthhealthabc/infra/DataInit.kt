package com.chatchatabc.healthhealthabc.infra

import com.chatchatabc.healthhealthabc.domain.service.RoleService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInit (
    val roleService: RoleService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        roleService.createRoles()
        // TODO: Create Admin User
    }
}