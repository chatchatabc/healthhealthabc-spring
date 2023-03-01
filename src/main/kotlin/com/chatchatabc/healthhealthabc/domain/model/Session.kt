package com.chatchatabc.healthhealthabc.domain.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.Instant

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sessions")
open class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open var id: String? = null

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    open var user: User? = null

    @Column
    open var ipAddress: String? = null

    @Column
    open var createdAt: Instant? = null

    // Persists
    @PrePersist
    protected fun onCreate() {
        createdAt = Instant.now()
    }
}