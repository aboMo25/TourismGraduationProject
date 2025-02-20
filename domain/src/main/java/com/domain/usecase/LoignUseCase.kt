package com.domain.usecase

import com.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(username: String, password: String) =
        userRepository.login(username, password)
}
