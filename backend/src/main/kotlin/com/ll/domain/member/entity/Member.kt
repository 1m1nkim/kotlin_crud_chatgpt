package com.ll.domain.member.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Entity
@Table(name = "members")
data class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    var name: String,
    var phoneNumber: String = "",
    var oAuthId: String,
    var email: String,
    var profileImageUrl: String,
    @Enumerated(EnumType.STRING)
    var memberType: MemberType = MemberType.USER
){
    enum class MemberType {USER, ADMIN}

    fun getAuthorities(): List<GrantedAuthority>{
        return if(memberType == MemberType.ADMIN){
            listOf(SimpleGrantedAuthority("ADMIN"))
        }else{
            listOf(SimpleGrantedAuthority("ROLE_ADMIN"))
        }
    }
}