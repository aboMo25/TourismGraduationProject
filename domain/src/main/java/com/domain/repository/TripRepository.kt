package com.domain.repository

import com.domain.model.Review
import com.domain.model.Trip

interface TripRepository {
	suspend fun getTripById(id: String) : Trip
	suspend fun getAllTrips(): List<Trip>
}