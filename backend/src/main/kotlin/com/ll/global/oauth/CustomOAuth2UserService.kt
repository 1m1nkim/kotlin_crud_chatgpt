package com.ll.global.oauth

import com.ll.domain.member.service.MemberService
import com.ll.global.jwt.JwtService
import com.ll.global.security.SecurityUser
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService (
    private val memberService: MemberService,
    private val jwtService: JwtService
): DefaultOAuth2UserService() {
    override fun loadUser(request: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(request)
        val attributes = oAuth2User.attributes

        val kakaoAccount = attributes["kakao_account"] as? Map<*, *>
        val profile = kakaoAccount?.get("profile") as? Map<*, *>
        val oAuthId = oAuth2User.name
        val email = kakaoAccount?.get("eamil") as? String?: ""
        val name = profile?.get("nickname") as? String?: ""
        val profileImageUrl = profile?.get("profile_image_url") as? String ?: ""

        val member = memberService.modifyOrJoin(oAuthId, name, email, profileImageUrl)
        return SecurityUser(member)
    }

}