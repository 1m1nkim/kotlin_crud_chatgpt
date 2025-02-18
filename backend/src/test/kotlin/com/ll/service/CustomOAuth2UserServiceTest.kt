package com.ll.service

import com.ll.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import java.time.Instant

class CustomOAuth2UserServiceTest {

    private val userRepository: UserRepository = mock()
    private val customOAuth2UserService = CustomOAuth2UserService(userRepository)

    @Test
    fun `신규 사용자 정보가 주어지면 회원가입 처리되어야 한다`() {
        // 미리 정의한 테스트용 속성
        val attributes = mapOf(
            "id" to 123456789L,
            "kakao_account" to mapOf(
                "email" to "kakaouser@example.com",
                "profile" to mapOf("nickname" to "카카오유저")
            )
        )

        // OAuth2User 객체 모의 생성
        val oAuth2User: OAuth2User = DefaultOAuth2User(
            emptyList(),
            attributes,
            "id"
        )

        // 가짜 AccessToken
        val accessToken = OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            "dummy",
            Instant.now(),
            Instant.now().plusSeconds(3600)
        )

        // OAuth2UserRequest 생성
        val oAuth2UserRequest = OAuth2UserRequest(
            customOAuth2UserService.clientRegistration,
            accessToken
        )

        // 실제 테스트 대상 메서드 호출
        val result = customOAuth2UserService.loadUser(oAuth2UserRequest)

        // "email" 필드가 "kakaouser@example.com"인지 검증
        assertThat(result.attributes["email"]).isEqualTo("kakaouser@example.com")
    }
}
