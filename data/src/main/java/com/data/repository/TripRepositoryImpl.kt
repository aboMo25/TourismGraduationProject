package com.data.repository

import com.data.model.request.ApiService
import com.data.model.toDomain
import com.domain.model.Trip
import com.domain.repository.TripRepository
import kotlin.collections.map


class TripRepositoryImpl(
	private val apiService: ApiService
) : TripRepository {

	override suspend fun getTripById(id: String): Trip {
		return apiService.getTrips().data
			?.firstOrNull { it.id.toString() == id }
			?.toDomain()
			?: throw Exception("Trip not found or null")
	}

	override suspend fun getAllTrips(): List<Trip> {
		return apiService.getTrips().data
			?.filterNotNull()
			?.map { it.toDomain() }
			?: emptyList()
	}
}

