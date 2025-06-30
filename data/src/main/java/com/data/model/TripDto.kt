package com.data.model

import com.domain.model.DetailsScreenData
import com.domain.model.HotelImages
import com.domain.model.Hotels2
import com.domain.model.Review
import com.domain.model.Trip

data class TripDto(
	val id: String?,
	val name: String?,
	val reviews: String?,
	val rating: Double?,
	val distance: Double?,
	val imageUrl: String?,
	val price: Double?,
	val detailsScreenData: List<DetailsScreenDataDto>?,
	val hotels: HotelDto?,
)

data class DetailsScreenDataDto(
	val id: Int?,
	val image: String?,
	val title: String?,
	val listOfImages: List<String>?,
	val description: String?,
	val rating: Double?,
	val location: String?,
	val reviewList: List<ReviewDto>?,
	val timeToOpen: String?,
	val price: Int?,
)

data class ReviewDto(
	val id: String?,
	val date: String?,
	val imageUrl: String?,
	val comment: String?,
	val rating: Double?,
	val name: String?,
)

data class HotelDto(
	val imageUrl: String?,
	val id: Int?,
	val name: String?,
	val description: String?,
	val price: Double?,
	val location: String?,
	val ratings: String?,
	val numberOfReviews: Int?,
	val imagesOfHotelImages: List<HotelImageDto>?,
)

data class HotelImageDto(
	val imageUrl: String?,
)


fun TripDto.toDomain() = Trip(
	id = id ?:"0" ,
	name = name ?: "",
	reviews = reviews ?: "0",
	rating = rating ?: 0.0,
	distance = distance ?: 0.0,
	imageUrl = imageUrl ?: "",
	price = price ?: 0.0,
	detailsScreenData = detailsScreenData?.map { it.toDomain() } ?: emptyList(),
	hotels = hotels?.toDomain() ?: Hotels2(
		imageUrl = "",
		id = id!!,
		name = "",
		description = "",
		price = 0.0,
		location = "",
		ratings = "",
		numberOfReviews = 0,
		imagesOfHotelImages = emptyList()
	),
)

fun DetailsScreenDataDto.toDomain() = DetailsScreenData(
	image = image ?: "",
	title = title ?: "",
	listOfImages = listOfImages ?: emptyList(),
	description = description ?: "",
	rating = rating ?: 0.0,
	location = location ?: "",
	reviewList = reviewList?.map { it.toDomain() } ?: emptyList(),
	timeToOpen = timeToOpen ?: "",
	price = price ?: 10,
	id = id?.toString() ?: "0",
)

fun ReviewDto.toDomain() = Review(
	id = id ?: "",
	date = date ?: "",
	imageUrl = imageUrl ?: "",
	comment = comment ?: "",
	rating = rating ?: 0.0,
	name = name ?: "",
)

fun HotelDto.toDomain() = Hotels2(
	imageUrl = imageUrl ?: "",
	id = id?.toString() ?: "0",
	name = name ?: "",
	description = description ?: "",
	price = price ?: 0.0,
	location = location ?: "",
	ratings = ratings ?: "",
	numberOfReviews = numberOfReviews ?: 0,
	imagesOfHotelImages = imagesOfHotelImages?.map { it.toDomain() } ?: emptyList()
)

fun HotelImageDto.toDomain() = HotelImages(
	imageUrl = imageUrl ?: ""
)