package com.ll.global.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController {
    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<String> {
        // 쿠키 삭제
        val deleteAccessToken = ResponseCookie.from("accessToken", "")
            .path("/")
            .maxAge(0)
            .secure(true)
            .build()
        val deleteRefreshToken = ResponseCookie.from("refreshToken", "")
            .path("/")
            .maxAge(0)
            .secure(true)
            .build()

        response.addHeader("Set-Cookie", deleteAccessToken.toString())
        response.addHeader("Set-Cookie", deleteRefreshToken.toString())

        return ResponseEntity.ok("로그아웃 되었습니다.")
    }
}
