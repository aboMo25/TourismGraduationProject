package com.domain.model

data class PassengerDetails(
	val fullName: String,
	val email: String,
	val phoneNumber: String,
	val nationality: String,
	val totalAdultPassengers: Int,
	val totalYoungPassengers: Int
)