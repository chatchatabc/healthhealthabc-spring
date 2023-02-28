package com.chatchatabc.healthhealthabc.domain.model

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
    open var id: String? = null

    @Column(unique = true)
    open var name: String = name

    @ManyToMany(mappedBy = "roles")
    open var users: MutableSet<User> = mutableSetOf()

    /**
     * Get the name of the role.
     */
    override fun getAuthority(): String? {
        return this.name
    }
}