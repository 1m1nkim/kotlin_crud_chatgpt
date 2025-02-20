package com.ll.global.oauth

import com.ll.global.jwt.JwtService
import com.ll.global.security.SecurityUser
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
    private val jwtService: JwtService
): SimpleUrlAuthenticationSuccessHandler(){
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val securityUser = authentication.principal as SecurityUser

        val accessToken = jwtService.generateToken(securityUser.member)
        val refreshToken = jwtService.generateRefreshToken(securityUser.member)

        val accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(60 * 60L)
            .build()

        val refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(7 * 24 * 60 * 60L)
            .build()

        response.addHeader("Set-Cookie", accessTokenCookie.toString())
        response.addHeader("Set-Cookie", refreshTokenCookie.toString())

        redirectStrategy.sendRedirect(request, response, "http://localhost:3000/")
    }
}