package com.ll.global.security

import com.ll.domain.member.entity.Member
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.core.user.OAuth2User

class SecurityUser(val member: Member): User (member.oAuthId, "", member.getAuthorities()), OAuth2User{
    override fun getAttributes(): Map<String?, Any?>? {
        return mapOf(
            "id" to member.id,
            "name" to member.name,
            "email" to member.email,
            "memberType" to member.memberType.name
        )
    }

    override fun getName(): String {
        return member.oAuthId
    }
}