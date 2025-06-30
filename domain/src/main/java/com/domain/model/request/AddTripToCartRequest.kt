package com.domain.model.request

import com.domain.model.DetailsScreenData
import com.domain.model.Hotels2
import kotlinx.serialization.Serializable

@Serializable
data class AddTripToCartRequest(
	val userId: String,
	val tripId: Int, // Use trip's ID
	val name: String,
	val price: Double,
	val imageUrl: String,
	val detailsScreenData: List<DetailsScreenData>,
	val hotels: Hotels2?, // Include the hotel associated with the trip
	val itemType: String = "TRIP" // Explicitly tell backend it's a trip

)