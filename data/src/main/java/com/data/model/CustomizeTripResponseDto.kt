package com.data.model

import com.domain.model.AllPlaces2
import com.domain.model.Review

data class CustomizeTripResponseDto(
	val API: Boolean,
	val data: List<MainPlace2Dto>?
)
