package com.ll.config

import com.ll.service.CustomOAuth2UserService
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
class SecurityConfig(
    private val customOAuth2UserServiceProvider: ObjectProvider<CustomOAuth2UserService>
) {
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring().requestMatchers(
                AntPathRequestMatcher("/h2-console/**")
            )
        }
    }
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
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
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }
            .csrf { csrf ->
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .loginPage("/login").permitAll()
                    .userInfoEndpoint { userInfo ->
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
