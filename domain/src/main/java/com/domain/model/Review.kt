package com.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Review(
	val id: String?, // Make nullable if it can be null
	val date: String?,
	val imageUrl: String?,
	val comment: String?,
	val rating: Double?, // Make nullable
	val name: String?
)