package com.data.model.response

data class Attraction(
	val id: String,
	val name: String,
	val description: String,
	val price: Double,
	var isVisited: Boolean = false
)