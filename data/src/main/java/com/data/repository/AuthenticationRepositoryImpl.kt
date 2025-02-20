package com.data.repository

import com.domain.repository.AuthenticationRemoteDataSource
import com.domain.repository.AuthenticationRepository
import com.domain.resources.CustomError
import com.domain.resources.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
) : AuthenticationRepository {

    override suspend fun authenticateWithBackend(googleToken: String): DataResult<String> =
        withContext(ioDispatcher) {
            try {
                val apiToken = authenticationRemoteDataSource.authenticateWithBackend(googleToken)
                DataResult.Success(apiToken)
            } catch (e: Exception) {
                DataResult.Error(CustomError.AUTHENTICATION_ERROR)
            }
        }
}