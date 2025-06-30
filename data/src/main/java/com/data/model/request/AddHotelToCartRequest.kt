package com.data.model.request

import kotlinx.serialization.Serializable

@Serializable // Add this
data class AddHotelToCartRequest(
	val userId: String, // CHANGE: From Int to String
	val hotelId: Int,
	val name: String?,
	val price: Double?,
	val imageUrl: String?,
	val description: String?,
	val location: String?,
	val ratings: String?,
	val numberOfReviews: Int?,
	val imagesOfHotelImages: List<String>?,
	val itemType: String = "HOTEL" // Explicitly tell backend it's a hotel
)