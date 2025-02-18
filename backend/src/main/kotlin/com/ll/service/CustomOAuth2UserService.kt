package com.ll.service

import com.ll.entity.User
import com.ll.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        // Kakao 서버로부터 사용자 정보를 받아옴
        val oAuth2User = super.loadUser(userRequest)
        val attributes = oAuth2User.attributes

        // Kakao의 응답 구조 예시:
        // {
        //   "id": 123456789,
        //   "properties": {
        //       "nickname": "홍길동",
        //       "profile_image": "https://..."
        //   },
        //   "kakao_account": { ... }
        // }
        val kakaoId = attributes["id"].toString()
        val properties = attributes["properties"] as? Map<*, *>
        val nickname = properties?.get("nickname") as? String ?: "Unknown"
        val profileImage = properties?.get("profile_image") as? String ?: ""

        // 예시로, Kakao 사용자 고유값을 기반으로 이메일을 생성
        val email = "kakao_$kakaoId@kakao.com"

        // 이미 가입된 사용자가 있는지 확인
        var user = userRepository.findByEmail(email)
        if (user == null) {
            // 신규 사용자 등록 (패스워드는 OAuth2용 더미값 사용)
            user = User(
                email = email,
                password = passwordEncoder.encode("oauth2dummy"),
                name = nickname,
                role = "ROLE_USER"
            )
            user = userRepository.save(user)
        } else {
            // 필요한 경우 회원 정보 업데이트 처리 (예: 닉네임, 프로필 이미지)
        }

        // DefaultOAuth2User를 생성하여 반환 (여기서 "id"는 YAML의 user-name-attribute와 일치)
        return DefaultOAuth2User(
            oAuth2User.authorities,
            attributes,
            "id"
        )
    }
}
