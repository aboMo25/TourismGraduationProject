package com.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GenericSuccessResponse(
	val success: Boolean,
	val message: String
)