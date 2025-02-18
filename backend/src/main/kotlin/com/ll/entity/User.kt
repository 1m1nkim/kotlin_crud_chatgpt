package com.ll.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(unique = true, nullable = false)
    val email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    val name: String,

    // 간단한 역할(Role) 문자열을 저장 (ex. ROLE_USER)
    @Column(nullable = false)
    val role: String = "ROLE_USER"
)
