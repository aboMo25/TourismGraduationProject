package com.domain.repository

import com.domain.resources.DataResult

interface AuthenticationRepository {
    suspend fun authenticateWithBackend(googleToken: String): DataResult<String>
}