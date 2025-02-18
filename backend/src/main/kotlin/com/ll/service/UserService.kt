package com.ll.service

import com.ll.entity.User
import com.ll.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    fun registerUser(email: String, rawPassword: String, name: String): User {
        // 이메일 중복 체크
        if (userRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("이미 가입된 이메일입니다.")
        }
        val encodedPassword = passwordEncoder.encode(rawPassword)
        val user = User(email = email, password = encodedPassword, name = name)
        return userRepository.save(user)
    }
}
