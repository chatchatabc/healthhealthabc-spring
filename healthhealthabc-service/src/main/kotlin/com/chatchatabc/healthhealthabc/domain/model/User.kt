package com.chatchatabc.healthhealthabc.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.Instant

// The user entity flag values arranged by index.
const val USER_DEACTIVATED = 0
const val USER_LOCKED = 1
const val USER_ACCOUNT_EXPIRED = 2
const val USER_CREDENTIALS_EXPIRED = 3


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
open class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @Column(unique = true)
    open lateinit var email: String

    @Column(unique = true)
    open lateinit var username: String

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    open lateinit var password: String

    @Column
    open lateinit var createdAt: Instant

    @Column
    open lateinit var updatedAt: Instant

    @Column
    open var emailConfirmedAt: Instant? = null

    @JsonIgnore
    @Column
    open var flag: Int = 0

//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
//    @JoinTable(
//        name = "user_roles",
//        joinColumns = [JoinColumn(name = "user_id")],
//        inverseJoinColumns = [JoinColumn(name = "role_id")]
//    )
//    open var roles: MutableSet<Role> = mutableSetOf()

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

    private fun getBitValue(bitIndex: Int): Boolean {
        return this.flag and (1 shl bitIndex) == 0
    }

    private fun setBitValue(bitIndex: Int, value: Boolean) {
        if (value) {
            this.flag = this.flag or (1 shl bitIndex)
        } else {
            this.flag = this.flag and (1 shl bitIndex).inv()
        }
    }
}