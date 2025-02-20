package com.ll.global.jwt


import com.ll.domain.member.service.MemberService
import com.ll.global.security.SecurityUser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val memberService: MemberService
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val token = extractTokenFromCookies(request)
        if (token != null && jwtService.validateToken(token)) {
            val kakaoId = jwtService.getKakaoIdFromToken(token)
            val member = memberService.findByoAuthId(kakaoId).orElse(null)
            if (member != null) {
                val securityUser = SecurityUser(member)
                val auth = UsernamePasswordAuthenticationToken(securityUser, null, securityUser.authorities)
                SecurityContextHolder.getContext().authentication = auth
            }
        }
        chain.doFilter(request, response)
    }

    private fun extractTokenFromCookies(request: HttpServletRequest): String? {
        return request.cookies?.firstOrNull { it.name == "accessToken" }?.value
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        // 예외 경로 설정 (예: /login, /books 등)
        return path.startsWith("/login")
    }
}
