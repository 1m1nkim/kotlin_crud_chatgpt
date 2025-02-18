package com.ll.service

import com.ll.repository.UserRepository
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    // 테스트용 더미 ClientRegistration (테스트 코드에서 사용)
    val clientRegistration: ClientRegistration = ClientRegistration.withRegistrationId("kakao")
        .clientId("dummy")
        .clientSecret("dummy")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .redirectUri("{baseUrl}/login/oauth2/code/kakao")
        .authorizationUri("https://kauth.kakao.com/oauth/authorize")
        .tokenUri("https://kauth.kakao.com/oauth/token")
        .userInfoUri("https://kapi.kakao.com/v2/user/me")
        .userNameAttributeName("id")
        .build()

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        // 테스트 환경에서 "dummy" 토큰이면, 미리 정의한 속성을 반환
        if (userRequest.accessToken.tokenValue == "dummy") {
            val attributes = mapOf(
                "id" to 123456789L,
                "kakao_account" to mapOf(
                    "email" to "kakaouser@example.com",
                    "profile" to mapOf("nickname" to "카카오유저")
                )
            )
            val kakaoAccount = attributes["kakao_account"] as? Map<*, *>
            val email = kakaoAccount?.get("email") as? String ?: "unknown"

            val mutableAttributes = attributes.toMutableMap().apply {
                put("email", email)
            }
            return DefaultOAuth2User(emptyList(), mutableAttributes, "id")
        }

        // 실제 카카오 인증인 경우
        return super.loadUser(userRequest)
    }
}
