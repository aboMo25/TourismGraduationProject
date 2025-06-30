package com.domain.model.response

import com.domain.model.AllPlaces2
import com.domain.model.CartItem2
import com.domain.model.DetailsScreenData
import com.domain.model.HotelImages
import com.domain.model.Hotels2
import com.domain.model.Review
import com.domain.model.TripCartItem
import com.domain.model.* // Import all domain models
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// --- Response Models for Nested Objects ---

@Serializable
data class CartItemResponseDto(
	// This DTO handles all types of cart items from the backend
	val id: Int,
	val name: String,
	val price: Double?,
	val imageUrl: String,
	val itemType: String, // Crucial discriminator: "PLACE", "HOTEL", "TRIP"

	// Place-specific fields (nullable, only present if itemType is "PLACE")
	val description: String? = null,
	val placeData: DetailsScreenData? = null, // Directly using domain model if backend matches

	// Hotel-specific fields (nullable, only present if itemType is "HOTEL")
	val location: String? = null,
	val ratings: String? = null,
	val numberOfReviews: Int? = null,
	val imagesOfHotelImages: List<HotelImages>? = null, // Directly using domain model if backend matches

	// Trip-specific fields (nullable, only present if itemType is "TRIP")
	val detailsScreenData: List<DetailsScreenData>? = null, // Nested places in a trip (domain models)
	val hotels: Hotels2? = null, // Nested hotel in a trip (domain model)
) {
	// Mapping function from DTO to domain model (CartItem2)
	fun toDomainModel(): CartItem2 {
		return when (itemType) {
			"PLACE" -> AllPlaces2(
				id = id.toString(),
				name = name,
				price = price,
				imageUrl = imageUrl,
				description = description ?: "",
				placeData = placeData ?: DetailsScreenData(
					id = null,
					image = "",
					title = "",
					listOfImages = emptyList(),
					description = "",
					rating = 0.0,
					location = "",
					reviewList = emptyList(),
					timeToOpen = "",
					price = 0
				),
			)

			"HOTEL" -> Hotels2(
				id = id.toString(),
				name = name,
				price = price,
				imageUrl = imageUrl,
				description = description ?: "",
				location = location ?: "",
				ratings = ratings ?: "",
				numberOfReviews = numberOfReviews ?: 0,
				imagesOfHotelImages = imagesOfHotelImages ?: emptyList()
			)

			"TRIP" -> TripCartItem(
				id = id.toString(),
				name = name,
				price = price,
				imageUrl = imageUrl,
				detailsScreenData = detailsScreenData ?: emptyList(),
				hotels = hotels
			)

			else -> throw IllegalArgumentException("Unknown cart item type: $itemType")
		}
	}
}

@Serializable
data class DetailsScreenDataResponse(
	@SerialName("_id") // This maps the backend's "_id" field
	val id: String?,   // This field will hold the unique string ID (e.g., "685d77d19594604509f1602d")
	val image: String?,
	val title: String?,
	val listOfImages: List<String>?,
	val description: String?,
	val rating: Double?,
	val location: String?,
	val reviewList: List<ReviewResponse>?,
	val timeToOpen: String?,
	val price: Int?
) {
	fun toDomainModel() = DetailsScreenData(
		id = id, // Now, `id` here *is* the `_id` string from backend
		image = image ?: "",
		title = title ?: "",
		listOfImages = listOfImages ?: emptyList(),
		description = description ?: "",
		rating = rating ?: 0.0,
		location = location ?: "",
		reviewList = reviewList?.map { it.toDomainModel() } ?: emptyList(),
		timeToOpen = timeToOpen ?: "",
		price = price ?: 0
	)
}

@Serializable
data class ReviewResponse(
	val id: String,
	val date: String,
	val imageUrl: String,
	val comment: String,
	val rating: Double,
	val name: String,
) {
	fun toDomainModel() = Review(
		id = id,
		date = date,
		imageUrl = imageUrl,
		comment = comment,
		rating = rating,
		name = name
	)
}

@Serializable
data class HotelImageResponse(
	val imageUrl: String,
) {
	fun toDomainModel() = HotelImages(imageUrl = imageUrl)
}


// --- Cart Item Response Models (Polymorphic) ---
// This is the top-level sealed interface/class for cart items from the backend


// This is the top-level sealed interface for cart items from the backend
@Serializable
sealed interface CartItemResponse {
	val id: Int? // Can be nullable at the DTO level
	val name: String
	val price: Double?
	val imageUrl: String
	val itemType: String // CRUCIAL: Discriminator field
	fun toDomainModel(): CartItem2 // Abstract mapping function
}

@Serializable
@SerialName("PLACE") // Maps to itemType = "PLACE" in JSON
data class AllPlaceCartItemResponse(
	override val id: Int?, // This `id` is the CartItemResponse's top-level ID (e.g., from mainPlace._id or AllPlaces2._id)
	override val name: String,
	override val price: Double?,
	override val imageUrl: String,
	override val itemType: String = "PLACE",
	val description: String?,
	val placeData: DetailsScreenDataResponse? // Use the DTO for nested objects
) : CartItemResponse {
	override fun toDomainModel(): AllPlaces2 {
		val mappedPlaceData = placeData?.toDomainModel() // Map the nested DTO
			?: throw IllegalStateException("placeData cannot be null when mapping AllPlaceCartItemResponse to AllPlaces2")

		return AllPlaces2(
			id = mappedPlaceData.id ?: throw IllegalStateException("PlaceData ID cannot be null"), // <--- THIS IS THE KEY CHANGE
			name = name,
			price = price,
			imageUrl = imageUrl,
			description = description ?: "",
			placeData = mappedPlaceData
		)	}
}

@Serializable
@SerialName("HOTEL") // Maps to itemType = "HOTEL" in JSON
data class HotelCartItemResponse(
	override val id: Int?, // Keep nullable here
	override val name: String,
	override val price: Double?,
	override val imageUrl: String,
	override val itemType: String = "HOTEL",
	val description: String?,
	val location: String?,
	val ratings: String?, // Consider HotelRatingResponse DTO if complex
	val numberOfReviews: Int?,
	val imagesOfHotelImages: List<HotelImageResponse>?
) : CartItemResponse {
	override fun toDomainModel(): Hotels2 {
		val actualId = id ?: throw IllegalStateException("Hotel ID cannot be null in HotelCartItemResponse")
		return Hotels2(
			id = actualId.toString(),
			name = name,
			price = price,
			imageUrl = imageUrl,
			description = description ?: "",
			location = location ?: "",
			ratings = ratings ?: "",
			numberOfReviews = numberOfReviews ?: 0,
			imagesOfHotelImages = imagesOfHotelImages?.map { it.toDomainModel() } ?: emptyList()
		)
	}
}

@Serializable
@SerialName("TRIP") // Response model for TripCartItem
data class TripCartItemResponse(
	override val id: Int?, // Keep nullable here
	override val name: String,
	override val price: Double?,
	override val imageUrl: String,
	override val itemType: String = "TRIP",
	val detailsScreenData: List<DetailsScreenDataResponse>?, // Use DTO for nested places
	val hotels: HotelCartItemResponse? // Use DTO for nested hotel
) : CartItemResponse {
	override fun toDomainModel(): TripCartItem {
		return TripCartItem(
			id = id?.toString() ?: throw IllegalStateException("Trip ID cannot be null in TripCartItemResponse"),
			name = name,
			price = price,
			imageUrl = imageUrl,
			detailsScreenData = detailsScreenData?.map { it.toDomainModel() } ?: emptyList(),
			hotels = hotels?.toDomainModel() // Map nested hotel response to domain model
		)
	}
}