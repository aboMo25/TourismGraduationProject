package com.data.model

import com.domain.model.AllPlaces2
import com.domain.model.DetailsScreenData
import com.domain.model.MainPlace2
import kotlinx.serialization.SerialName


data class MainPlace2Dto(
	val id :Int,
	val _id: String,
	val imageUrl: String,
	val name: String,
	val allPlaces: List<AllPlaces2>
)
fun MainPlace2Dto.toDomain() = MainPlace2(
	id = id.toString(),
	imageUrl = imageUrl,
	name = name,
	allPlaces = allPlaces
)

