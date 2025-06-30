package com.domain.repository

import com.domain.model.AllPlaces2
import com.domain.model.CartItem2
import com.domain.model.Hotels2
import com.domain.model.TripCartItem
import com.domain.network.ResultWrapper

interface CartRepository3 {
	suspend fun addPlaceToCart(userId: String, place: AllPlaces2): ResultWrapper<Boolean>
	suspend fun addHotelToCart(userId: String, hotel: Hotels2): ResultWrapper<Boolean>
	suspend fun addTripToCart(userId: String, trip: TripCartItem): ResultWrapper<Boolean> // NEW method
	suspend fun getCartItems(userId: String): ResultWrapper<List<CartItem2>>
	suspend fun removeItemFromCart(userId: String, itemId: String): ResultWrapper<Boolean>
	suspend fun updateItemQuantity(userId: String, itemId: String, newQuantity: Int): ResultWrapper<Boolean>
	suspend fun clearCart(userId: String): ResultWrapper<Boolean>
}