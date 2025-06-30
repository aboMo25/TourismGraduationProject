package com.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class CartItem2 {
	abstract val id: String?
	abstract val name: String
	abstract val price: Double? // Use Double for consistency across all items
	abstract val imageUrl: String
	abstract val itemType: String // Discriminator for polymorphic serialization/deserialization
}
@Serializable
@SerialName("TRIP") // Keeps discriminator
data class TripCartItem(
	override val id: String,
	override val name: String,
	override val price: Double?,
	override val imageUrl: String,
	val detailsScreenData: List<DetailsScreenData>, // List of places in the trip
	val hotels: Hotels2?, // Optional hotel in the trip
	override val itemType: String = "TRIP" // Default value for discriminator
) : CartItem2()

@Serializable
@SerialName("PLACE") // Keeps discriminator if you use it this way in other contexts
data class AllPlaces2(
	override val id: String, // Must be non-nullable as it's the primary identifier
	override val name: String,
	override val price: Double?, // Kept Double? for consistency with CartItem2
	override val imageUrl: String,
	val description: String, // Made non-nullable, provide default in DTO if needed
	val placeData: DetailsScreenData, // Now directly using the mapped domain model
	override val itemType: String = "PLACE" // Default value for discriminator
) : CartItem2()

@Serializable
@SerialName("HOTEL") // Keeps discriminator
data class Hotels2(
	override val id: String,
	override val name: String,
	override val price: Double?,
	override val imageUrl: String,
	val description: String,
	val location: String,
	val ratings: String, // Consider making this Double if it represents a numeric rating
	val numberOfReviews: Int,
	val imagesOfHotelImages: List<HotelImages>,
	override val itemType: String = "HOTEL" // Default value for discriminator
) : CartItem2()
// And when you define your Transport model, it will also extend CartItem
