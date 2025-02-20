package com.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class User(
	val email: String,
	val displayName: String
)