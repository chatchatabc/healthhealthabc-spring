package com.chatchatabc.healthhealthabc.domain.model

import jakarta.persistence.*
import lombok.Builder
import lombok.Data

@Entity
@Data
@Builder
@Table(name = "roles")
open class Role (name: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @Column(unique = true)
    open var name: String = name

//    @JsonIgnore
//    @ManyToMany(mappedBy = "roles")
//    open var users: MutableSet<User> = mutableSetOf()

}