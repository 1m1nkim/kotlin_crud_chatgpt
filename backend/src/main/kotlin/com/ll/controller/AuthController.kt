package com.ll.controller

import com.ll.entity.User
import com.ll.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<User> {
        val user = userService.registerUser(registerRequest.email, registerRequest.password, registerRequest.name)
        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }
}

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)
