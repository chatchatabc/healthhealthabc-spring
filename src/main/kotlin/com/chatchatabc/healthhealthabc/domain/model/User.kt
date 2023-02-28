package com.chatchatabc.healthhealthabc.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
open class User : UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open var id: String? = null

    @Column(unique = true)
    open var email: String? = null

    @Column(unique = true)
    private var username: String? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private var password: String? = null

    @Column
    open var createdAt: Instant? = null

    // TODO: Add Roles

    /**
     * Get the roles of the user.
     */
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        // TODO: Implement logic to get roles
        return emptyList<GrantedAuthority>().toMutableList()
    }

    /**
     * Get the username of the user.
     */
    override fun getUsername(): String? {
        return this.username
    }

    /**
     * Get the password of the user.
     */
    override fun getPassword(): String? {
        return this.password
    }

    /**
     * TODO: Add logic to check if user is expired.
     */
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    /**
     * TODO: Add logic to check if user is locked.
     */
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    /**
     * TODO: Add logic to check if user's credentials are expired.
     */
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    /**
     * TODO: Add logic to check if user is enabled.
     */
    override fun isEnabled(): Boolean {
        return true
    }

    // Persists
    @PrePersist
    protected fun onCreate() {
        this.createdAt = Instant.now()
    }

    /**
     * Set the password of the user.
     */
    open fun setPassword(encodedPassword: String?) {
        this.password = encodedPassword
    }
}