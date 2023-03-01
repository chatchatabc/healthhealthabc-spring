package com.chatchatabc.healthhealthabc.domain.model.log.user

import com.chatchatabc.healthhealthabc.domain.model.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import lombok.Builder
import lombok.Data
import java.time.Instant

@Entity
@Data
@Builder
@Table(name = "user_login_logs")
open class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open var id: String? = null

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    open var user: User? = null

    @Column
    open var email: String? = null

    @Column
    open var ipAddress: String? = null

    @Column
    open var createdAt: Instant? = null

    @Column
    open var success: Boolean? = null

    // Persists
    @PrePersist
    protected fun onCreate() {
        createdAt = Instant.now()
    }
}