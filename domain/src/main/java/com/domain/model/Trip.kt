package com.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class Trip(
	val id: String,
	val name: String,
	val reviews: String,
	val rating: Double,
	val distance: Double,
	val imageUrl: String,
	val price: Double,
	val detailsScreenData: List<DetailsScreenData>,
	val hotels: Hotels2,
)

data class CustomizedTripData(
	val id: String,
	val name: String,
	val imageUrl: String,
)

data class Hotels(
	val imageUrl: String,
	val id: String,
	val name: String,
	val description: String,
	val price: Double,
	val location: String,
	val ratings: String,
	val numberOfReviews: Int,
	val imagesOfHotelImages: List<HotelImages>,
)

data class HotelImages(
	val imageUrl: String,
)

data class SplashScreenData(
	val image: String,
	val title: String,
	val description: String,
)

