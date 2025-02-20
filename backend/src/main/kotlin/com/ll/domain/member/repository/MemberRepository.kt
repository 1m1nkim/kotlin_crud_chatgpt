package com.ll.domain.member.repository

import com.ll.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findByoAuthId(oauthId: String): Optional<Member>
    fun findByEmail(email: String): Optional<Member>
}