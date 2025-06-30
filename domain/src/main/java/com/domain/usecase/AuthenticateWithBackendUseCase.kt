package com.domain.usecase

import com.domain.model.response.AuthResponse
import com.domain.repository.AuthenticationRepository
import com.domain.resources.DataResult


//class AuthenticateWithBackendUseCase(private val authenticationRepository: AuthenticationRepository) {
//    suspend operator fun invoke(googleToken: String): DataResult<AuthResponse> = // Changed return type
//        authenticationRepository.authenticateWithBackend(googleToken).checkResultAndReturn(
//            onSuccess = { DataResult.Success(it) },
//            onError = { DataResult.Error(it) }
//        )
//}


