package com.domain.repository

interface AuthenticationRemoteDataSource {
    suspend fun authenticateWithBackend(googleToken: String): String
}