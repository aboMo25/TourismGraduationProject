package com.domain.repository

import com.domain.model.response.AuthResponse
import com.domain.network.ResultWrapper

interface UserRepository {
    suspend fun login(
        email: String,
        password: String
    ): ResultWrapper<AuthResponse>

    suspend fun register(
        email: String,
        password: String,
        name: String
    ): ResultWrapper<AuthResponse>

}