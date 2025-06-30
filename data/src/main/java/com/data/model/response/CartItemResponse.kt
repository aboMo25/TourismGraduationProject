//package com.data.model.response
//
//import kotlinx.serialization.Serializable
//import com.domain.model.* // Import your domain models
//
//@Serializable
//sealed class CartItemResponse {
//	abstract val quantity: Int
//	abstract val id: String
//	abstract val name: String
//	abstract val price: Double
//	abstract val imageUrl: String
//
//	// Add a discriminator property if your API sends a type field
//	abstract val type: String // e.g., "place", "hotel", "transport"
//
//	// Abstract function to convert to the domain model
//	abstract fun toDomainModel(): CartItem2
//}
//
//@Serializable
//data class AllPlaceResponse(
//	override val imageUrl: String,
//	override val id: String,
//	override val name: String,
//	val placeData: String, // Assuming DetailsScreenData is sent as a string/JSON in API
//	val description: String,
//	override val price: Double,
//	override val quantity: Int,
//	override val type: String = "place" // Discriminator
//) : CartItemResponse() {
//	override fun toDomainModel(): AllPlaces2 {
//		// You'll need to parse 'placeData' if it's a complex JSON string
//		val parsedPlaceData = DetailsScreenData(placeData) // Assuming simple string or parse it
//		return AllPlaces2(
//			imageUrl = imageUrl,
//			id = id,
//			name = name,
//			placeData = parsedPlaceData,
//			description = description,
//			price = price,
//			quantity = quantity
//		)
//	}
//}
//
//@Serializable
//data class HotelResponse(
//	override val imageUrl: String,
//	override val id: String,
//	override val name: String,
//	val description: String,
//	override val price: Double,
//	val location: String,
//	val ratings: String,
//	val numberOfReviews: Int,
//	val imagesOfHotelImages: List<HotelImageResponse>, // Use HotelImageResponse here
//	override val quantity: Int,
//	override val type: String = "hotel" // Discriminator
//) : CartItemResponse() {
//	override fun toDomainModel(): Hotels2 {
//		return Hotels2(
//			imageUrl = imageUrl,
//			id = id,
//			name = name,
//			description = description,
//			price = price,
//			location = location,
//			ratings = ratings,
//			numberOfReviews = numberOfReviews,
//			imagesOfHotelImages = imagesOfHotelImages.map { it.toDomainModel() }, // Map inner images
//			quantity = quantity
//		)
//	}
//}
//
//@Serializable
//data class TransportResponse(
//	override val imageUrl: String,
//	override val id: String,
//	override val name: String,
//	override val price: Double,
//	override val quantity: Int,
//	override val type: String = "transport", // Discriminator
//	// ... other transport-specific properties from API
//) : CartItemResponse() {
//	override fun toDomainModel(): Transports {
//		return Transports(
//			imageUrl = imageUrl,
//			id = id,
//			name = name,
//			price = price,
//			quantity = quantity,
//			// ... map other properties
//		)
//	}
//}
//
//// Define HotelImageResponse if your API sends it separately
//@Serializable
//data class HotelImageResponse(
//	val url: String // Assuming your API sends a URL string for images
//) {
//	fun toDomainModel(): HotelImages = HotelImages(url = url)
//}