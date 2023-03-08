package com.chatchatabc.healthhealthabc.domain.model.log.user

import com.chatchatabc.healthhealthabc.domain.model.User
import jakarta.persistence.*
import lombok.Builder
import lombok.Data
import java.time.Instant

@Entity
@Data
@Builder
@Table(name = "user_logout_logs")
open class LogoutLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    open lateinit var user: User

    @Column
    open lateinit var email: String

    @Column
    open lateinit var ipAddress: String

    @Column
    open lateinit var createdAt: Instant

    // Persists
    @PrePersist
    protected fun onCreate() {
        createdAt = Instant.now()
    }
}