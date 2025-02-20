package com.domain.repository

import com.domain.model.UserDomainModel
import com.domain.network.ResultWrapper

interface UserRepository {

    suspend fun login(
        email: String,
        password: String
    ): ResultWrapper<UserDomainModel>

    suspend fun register(
        email: String,
        password: String,
        name: String
    ): ResultWrapper<UserDomainModel>
}