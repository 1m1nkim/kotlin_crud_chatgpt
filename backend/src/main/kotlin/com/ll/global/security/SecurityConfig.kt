package com.ll.global.security

import com.ll.domain.member.service.MemberService
import com.ll.global.jwt.JwtAuthenticationFilter
import com.ll.global.jwt.JwtService
import com.ll.global.oauth.CustomOAuth2UserService
import com.ll.global.oauth.OAuth2SuccessHandler
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtService: JwtService,
    private val memberService: MemberService,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2SuccessHandler: OAuth2SuccessHandler
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/admin/login").permitAll()
                    .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(
                        "/api/public/**", "/oauth2/**", "/api/auth/**",
                        "/refresh", "/api/auth/refresh", "/swagger-ui/**", "/v3/api-docs/**",
                        "/api/auth/me", "/api/auth/me/**"
                    ).permitAll()
                    .requestMatchers("/my/orders").permitAll()
                    .requestMatchers("/books/**", "/event/**", "/images/**", "/cart/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/reviews/**", "/cart").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(JwtAuthenticationFilter(jwtService, memberService), UsernamePasswordAuthenticationFilter::class.java)
            .oauth2Login{ oauth2 -> oauth2
                .authorizationEndpoint{authorization  -> authorization
                    .baseUri("/oauth2/authorization")
                    .authorizationRequestRepository(HttpSessionOAuth2AuthorizationRequestRepository())}
                .redirectionEndpoint{ redirection -> redirection
                    .baseUri("/login/oauth2/code/*")}
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint{ userInfo -> userInfo
                    .userService(customOAuth2UserService)}
            }
            .exceptionHandling { exception ->
                exception.authenticationEntryPoint { request, response, authException ->
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.contentType = "application/json;charset=UTF-8"
                    response.writer.write("인증이 필요합니다.")
                }
            }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowCredentials = true
            allowedOrigins = listOf("http://localhost:3000")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowedHeaders = listOf("*")
            exposedHeaders = listOf("Authorization", "RefreshToken")
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun authenticationManager(http: HttpSecurity, memberService: MemberService): AuthenticationManager {
        val provider = DaoAuthenticationProvider().apply {
            setUserDetailsService { identifier ->
                val member = if (identifier.contains("@")) {
                    memberService.findByEmail(identifier)
                } else {
                    memberService.findByoAuthId(identifier)
                }.orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다.") }
                SecurityUser(member)
            }
            setPasswordEncoder(passwordEncoder())
        }
        return ProviderManager(provider)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
