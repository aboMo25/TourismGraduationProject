package com.domain.model

import androidx.annotation.DrawableRes

data class MainPlace2(
    val id: String,
    val imageUrl: String,
    val name: String,
	val allPlaces: List<AllPlaces2>
)
data class AllPlaces(
	val imageUrl: String,
	val mainPlaceId: String,
	val name: String,
	val placeData:DetailsScreenData,
	val description: String,
	val price: Int
)
