package com.chatchatabc.healthhealthabc.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Builder
import lombok.Data
import org.springframework.security.core.GrantedAuthority

@Entity
@Data
@Builder
@Table(name = "roles")
open class Role (name: String) : GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @Column(unique = true)
    open var name: String = name

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    open var users: MutableSet<User> = mutableSetOf()

    /**
     * Get the name of the role.
     */
    override fun getAuthority(): String? {
        return this.name
    }
}