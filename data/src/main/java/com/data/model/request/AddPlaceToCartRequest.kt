package com.data.model.request

import com.domain.model.DetailsScreenData
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddPlaceToCartRequest(
	val userId: String,
	@SerialName("placeId") // Backend expects "placeId", so this is correct
	val placeId: Int,   // This field will hold the backend's _id (e.g., "685d77d19594604509f1602d")
	val name: String,
	val price: Int,        // Still Int
	val imageUrl: String,
	val description: String,
	@Contextual val placeData: DetailsScreenData,
	val itemType: String
)
