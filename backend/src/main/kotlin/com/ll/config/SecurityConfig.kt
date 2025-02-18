package com.ll.config

import com.ll.service.CustomOAuth2UserService
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        customOAuth2UserServiceProvider: ObjectProvider<CustomOAuth2UserService>
    ): SecurityFilterChain {
        // 필요할 때 customOAuth2UserService를 가져옴
        val customOAuth2UserService = customOAuth2UserServiceProvider.getIfAvailable()

        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/login/**", "/oauth2/**",
                        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                        "/api/auth/register"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .loginPage("/login").permitAll()
                    .userInfoEndpoint { userInfo ->
                        // customOAuth2UserService가 존재할 때만 사용
                        customOAuth2UserService?.let {
                            userInfo.userService(it)
                        }
                    }
            }
            .logout { logout ->
                logout.logoutSuccessUrl("/").permitAll()
            }

        return http.build()
    }
}
