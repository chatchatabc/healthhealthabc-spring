package com.chatchatabc.healthhealthabc.infra.mapper

import com.chatchatabc.api.application.dto.user.RoleDTO
import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.healthhealthabc.domain.model.Role
import com.chatchatabc.healthhealthabc.domain.model.User
import org.modelmapper.Converter
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.stream.Collectors

@Configuration
class ModelMapperConfig {
    @Bean
    fun userModelMapper(): ModelMapper? {
        val modelMapper = ModelMapper()

        val rolesConverter = Converter<Set<Role>, Set<RoleDTO>> {
            it.source.stream()
                .map { role -> modelMapper.map(role, RoleDTO::class.java) }
                .collect(Collectors.toSet())
        }

        val typeMap = modelMapper.createTypeMap(User::class.java, UserDTO::class.java)
        typeMap.addMappings { mapper ->
            mapper.using(rolesConverter).map(User::roles, UserDTO::setRoles)
            mapper.map(User::emailConfirmedAt, UserDTO::setEmailConfirmedAt)
        }

        return modelMapper
    }
}