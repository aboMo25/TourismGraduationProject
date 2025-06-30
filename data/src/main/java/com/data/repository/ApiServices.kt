package com.data.repository

import com.data.model.CustomizeTripResponseDto
import com.data.model.TripsResponseDto
import retrofit2.http.GET

interface ApiServices {
	@GET("popular/places")
	suspend fun getTrips(): TripsResponseDto
}


