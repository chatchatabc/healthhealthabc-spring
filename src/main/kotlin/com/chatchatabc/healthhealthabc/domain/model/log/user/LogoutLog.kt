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
    lateinit var id: String

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    lateinit var user: User

    @Column
    lateinit var email: String

    @Column
    lateinit var ipAddress: String

    @Column
    lateinit var createdAt: Instant

    // Persists
    @PrePersist
    protected fun onCreate() {
        createdAt = Instant.now()
    }
}