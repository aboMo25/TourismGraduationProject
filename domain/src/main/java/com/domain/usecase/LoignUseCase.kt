package com.domain.usecase

import com.domain.model.response.AuthResponse
import com.domain.network.ResultWrapper
import com.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {
    suspend fun execute(email: String, password: String): ResultWrapper<AuthResponse> = // Changed return type
        userRepository.login(email, password)
}
