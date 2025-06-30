package com.data.repository

import com.data.model.request.ApiService
import com.data.model.toDomain
import com.domain.model.MainPlace2
import com.domain.repository.MainPlacesRepository

class MainPlacesRepositoryImpl(private val apiServices2: ApiService) : MainPlacesRepository {
	override suspend fun getMainPlaces(): List<MainPlace2> {
		val response = apiServices2.getCustomTrips()
		return response.data?.map { it.toDomain() } ?: emptyList()
	}
}
