package com.domain.usecase

import com.domain.model.AllPlaces2
import com.domain.model.CartItem2
import com.domain.model.Hotels2
import com.domain.network.ResultWrapper
import com.domain.repository.CartRepository3 // Make sure this is the correct repository

// --- Keep only these definitive versions of the Use Cases ---

class GetCartItemsUseCase(private val cartRepository: CartRepository3) {
	suspend operator fun invoke(userId: Int): ResultWrapper<List<CartItem2>> {
		return cartRepository.getCartItems(userId.toString())
	}
}

class AddPlaceToCartUseCase(private val cartRepository: CartRepository3) {
	suspend operator fun invoke(userId: String, place: AllPlaces2): ResultWrapper<Boolean> {
		val placeCartItem= AllPlaces2(
			id = place.id,
			name = place.name,
			price = place.price,
			imageUrl = place.imageUrl,
			description = place.description,
			placeData = place.placeData,
		)
		return cartRepository.addPlaceToCart(userId, placeCartItem)
	}
}

class AddHotelToCartUseCase(private val cartRepository: CartRepository3) {
	suspend operator fun invoke(userId: Int, hotel: Hotels2): ResultWrapper<Boolean> {
		return cartRepository.addHotelToCart(userId.toString(), hotel)
	}
}

class RemoveItemFromCartUseCase2(private val cartRepository: CartRepository3) {
	suspend operator fun invoke(userId: Int, itemId: String): ResultWrapper<Boolean> {
		return cartRepository.removeItemFromCart(userId.toString(), itemId)
	}
}

class UpdateItemQuantityUseCase(private val cartRepository: CartRepository3) {
	suspend operator fun invoke(userId: Int, itemId: String, newQuantity: Int): ResultWrapper<Boolean> {
		return cartRepository.updateItemQuantity(userId.toString(), itemId, newQuantity)
	}
}

class ClearCartUseCase(private val cartRepository: CartRepository3) {
	suspend operator fun invoke(userId: Int): ResultWrapper<Boolean> {
		return cartRepository.clearCart(userId.toString())
	}
}