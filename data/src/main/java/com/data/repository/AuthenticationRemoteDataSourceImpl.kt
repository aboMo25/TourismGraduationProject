package com.data.repository

import com.domain.repository.AuthenticationRemoteDataSource


class AuthenticationRemoteDataSourceImpl: AuthenticationRemoteDataSource {
    override suspend fun authenticateWithBackend(googleToken: String): String {
        //Here you hit your API endpoint, parse the result and return your API token or an error
        return "apiToken"
    }
}