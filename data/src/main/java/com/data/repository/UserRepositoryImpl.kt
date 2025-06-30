package com.data.repository

import com.data.model.request.ApiService
import com.data.model.request.LoginRequestAction
import com.data.model.request.RegisterRequestAction
import com.data.model.request.AddHotelToCartRequest
import com.data.model.request.AddPlaceToCartRequest
import com.domain.model.AllPlaces2
import com.domain.model.response.AuthResponse
import com.domain.model.Hotels2
import com.domain.network.ResultWrapper
import com.domain.network.safeApiCall
import com.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {
	override suspend fun login(email: String, password: String): ResultWrapper<AuthResponse> {
		return safeApiCall(Dispatchers.IO) {
			apiService.login(LoginRequestAction(email, password))

		}
	}

	override suspend fun register(email: String, password: String, name: String): ResultWrapper<AuthResponse> {
		return safeApiCall(Dispatchers.IO) {
			apiService.register(RegisterRequestAction(email, password, name))
		}
	}

}