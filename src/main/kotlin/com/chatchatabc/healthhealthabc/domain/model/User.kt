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

// The user entity flag values arranged by index.
const val USER_DEACTIVATED = 0

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
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

    @Column
    open var updatedAt: Instant? = null

    @Column
    open var emailConfirmedAt: Instant? = null

    @Column
    open var flag: Int = 0

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    open var roles: MutableSet<Role> = mutableSetOf()

    /**
     * Get the roles of the user.
     */
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roles.stream().map { role -> role as GrantedAuthority }.toList().toMutableList()
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
     * Check if the user is enabled.
     */
    override fun isEnabled(): Boolean {
        return getBitValue(USER_DEACTIVATED)
    }

    /**
     * Deactivate the user.
     */
    fun deactivateUser() {
        setBitValue(USER_DEACTIVATED, true)
    }

    /**
     * Activate the user.
     */
    fun activateUser() {
        setBitValue(USER_DEACTIVATED, false)
    }

    // Persists
    @PrePersist
    protected fun onCreate() {
        this.createdAt = Instant.now()
        this.updatedAt = Instant.now()
    }

    // Update
    @PreUpdate
    protected fun onUpdate() {
        this.updatedAt = Instant.now()
    }

    /**
     * Set the password of the user.
     */
    open fun setPassword(encodedPassword: String?) {
        this.password = encodedPassword
    }

    private fun getBitValue(bitIndex: Int): Boolean {
        return this.flag and (1 shl bitIndex) != 0
    }

    private fun setBitValue(bitIndex: Int, value: Boolean) {
        if (value) {
            this.flag = this.flag or (1 shl bitIndex)
        } else {
            this.flag = this.flag and (1 shl bitIndex).inv()
        }
    }
}