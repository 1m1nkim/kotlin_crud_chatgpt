package com.ll.domain.member.service

import com.ll.domain.member.entity.Member
import com.ll.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class MemberService(private val memberRepository: MemberRepository) {
    fun findByoAuthId(oAuthId: String): Optional<Member>{
        return memberRepository.findByoAuthId(oAuthId)
    }

    fun findByEmail(email: String): Optional<Member>{
        return memberRepository.findByEmail(email)
    }

    //가입 또는 회원정보 수정
    fun modifyOrJoin(oAuthId: String, name: String, email: String, profileImageUrl: String): Member {
        val existingMember = memberRepository.findByoAuthId(oAuthId)
        return if(existingMember.isPresent) {
            val member = existingMember.get()
            member.name = name
            member.profileImageUrl = profileImageUrl
            memberRepository.save(member)
        }else{
            val newMember = Member(
                name = name,
                oAuthId = oAuthId,
                email = email,
                profileImageUrl = profileImageUrl,
                memberType = Member.MemberType.USER
            )
            memberRepository.save(newMember)
        }
    }
}