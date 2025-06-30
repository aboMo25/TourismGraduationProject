package com.domain.usecase

import com.domain.model.Trip
import com.domain.repository.TripRepository

class GetAllTripsUseCase(private val repository: TripRepository) {
	suspend operator fun invoke(): List<Trip> {
		return repository.getAllTrips()
	}
}