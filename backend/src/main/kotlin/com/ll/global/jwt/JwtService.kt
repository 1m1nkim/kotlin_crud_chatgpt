package com.ll.global.jwt

import com.ll.domain.member.entity.Member
import com.nimbusds.jose.util.StandardCharset
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.Date

@Service
class JwtService(
    @Value("\${custom.jwt.secretKey}") secretKey: String
) {
    private val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
    private val TOKEN_VALIDITY = 60 * 60 * 1000L  // 1시간
    private val REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000L // 7일

    fun generateToken(member: Member): String{
        val now = Date()
        val validity = Date(now.time + TOKEN_VALIDITY)
        val token = Jwts.builder()
            .setSubject(member.oAuthId)
            .claim("id", member.id)
            .claim("email", member.email)
            .claim("name", member.name)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key)
            .compact()
        return URLEncoder.encode(token, StandardCharsets.UTF_8)
    }

    fun generateRefreshToken(member: Member): String{
        val token = Jwts.builder()
            .setSubject(member.oAuthId)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
            .signWith(key)
            .compact()
        return URLEncoder.encode(token, StandardCharsets.UTF_8)
    }

    fun validateToken(token: String): Boolean{
        return try{
            val decode = URLDecoder.decode(token, StandardCharsets.UTF_8)
            Jwts.parserBilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(decode)
            true
        }catch(e: Exception){
            false
        }
    }

    fun getKakaoIdFromToken(token: String): String{
        val decoded = URLDecoder.decode(token, StandardCharsets.UTF_8)
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(decoded).body.subject
    }
}