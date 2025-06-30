package com.domain.usecase

import com.domain.model.Trip // Assuming Trip is still your domain model
import com.domain.model.TripCartItem
import com.domain.network.ResultWrapper
import com.domain.repository.CartRepository3 // Assuming this is your cart repository

class AddTripToCartUseCase(private val cartRepository: CartRepository3) {
	suspend operator fun invoke(userId: String, trip: Trip): ResultWrapper<Boolean> {
		// Map the Trip domain model to TripCartItem for adding to cart
		val tripCartItem = TripCartItem(
			id = trip.id,
			name = trip.name,
			price = trip.price,
			imageUrl = trip.imageUrl,
			detailsScreenData = trip.detailsScreenData,
			hotels = trip.hotels
		)
		return cartRepository.addTripToCart(userId, tripCartItem)
	}
}