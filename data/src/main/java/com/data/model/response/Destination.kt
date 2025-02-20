package com.data.model.response

data class Destination(
	val id: String,
	val name: String,
	val description: String,
	val imageUrl: String,
	val attractions: List<Attraction>
)