package com.data.repository

import com.data.model.request.ApiService
import com.data.model.request.AddHotelToCartRequest
import com.data.model.request.AddPlaceToCartRequest
import com.domain.model.AllPlaces2
import com.domain.model.CartItem2
import com.domain.model.Hotels2
import com.domain.model.TripCartItem
import com.domain.model.request.AddTripToCartRequest
import com.domain.network.ResultWrapper
import com.domain.network.safeApiCall
import com.domain.repository.CartRepository3
import kotlinx.coroutines.Dispatchers

class CartRepository3Impl(private val apiService: ApiService) : CartRepository3 {

	override suspend fun addPlaceToCart(userId: String, place: AllPlaces2): ResultWrapper<Boolean> {
		if (userId.isBlank()) {
			return ResultWrapper.Failure(IllegalArgumentException("User ID is missing or empty."))
		}
		if (place.id.isBlank()) { // place.id is now the DetailsScreenData.backendId
			return ResultWrapper.Failure(IllegalArgumentException("Place ID for cart is missing or empty."))
		}
		if (place.name.isBlank()) {
			return ResultWrapper.Failure(IllegalArgumentException("Place name is missing or empty."))
		}
		val itemType = "PLACE"
		return safeApiCall(Dispatchers.IO) {
			apiService.addPlaceToCart(
				AddPlaceToCartRequest(
					userId = userId,
					placeId = place.id.toInt(), // <--- CHANGED FROM itemId TO placeId
					name = place.name,
					price = place.price!!.toInt(),
					imageUrl = place.imageUrl,
					description = place.description,
					placeData = place.placeData,
					itemType = itemType
				)
			).success
		}
	}
	override suspend fun addHotelToCart(userId: String, hotel: Hotels2): ResultWrapper<Boolean> {
		return safeApiCall(Dispatchers.IO) {
			apiService.addHotelToCart(
				AddHotelToCartRequest(
					userId = userId,
					hotelId = hotel.id.toInt(), // Use itemId
					name = hotel.name,
					price = hotel.price,
					imageUrl = hotel.imageUrl,
					description = hotel.description,
					location = hotel.location,
					ratings = hotel.ratings,
					numberOfReviews = hotel.numberOfReviews,
					imagesOfHotelImages = hotel.imagesOfHotelImages.map { it.imageUrl }, // Assuming imagesOfHotelImages is a list of objects with imageUrl
					itemType = "HOTEL" // Redundant if already in request, but good for clarity
				)
			).success
		}
	}

	override suspend fun addTripToCart(userId: String, trip: TripCartItem): ResultWrapper<Boolean> {
		return safeApiCall(Dispatchers.IO) {
			apiService.addTripToCart(
				AddTripToCartRequest(
					userId = userId,
					tripId = trip.id.toInt(), // Use itemId
					name = trip.name,
					price = trip.price!!,
					imageUrl = trip.imageUrl,
					detailsScreenData = trip.detailsScreenData,
					hotels = trip.hotels,
					itemType = "TRIP" // Redundant if already in request, but good for clarity
				)
			).success
		}
	}

	override suspend fun getCartItems(userId: String): ResultWrapper<List<CartItem2>> {
		return safeApiCall(Dispatchers.IO) {
			apiService.getCartItems(userId).map { it.toDomainModel() } // Map each DTO to domain model
		}
	}

	override suspend fun removeItemFromCart(userId: String, itemId: String): ResultWrapper<Boolean> {
		return safeApiCall(Dispatchers.IO) { apiService.removeItemFromCart(userId, itemId).success }
	}

	override suspend fun updateItemQuantity(userId: String, itemId: String, newQuantity: Int): ResultWrapper<Boolean> {
		return safeApiCall(Dispatchers.IO) { apiService.updateItemQuantity(userId, itemId, newQuantity).success }
	}

	override suspend fun clearCart(userId: String): ResultWrapper<Boolean> {
		return safeApiCall(Dispatchers.IO) { apiService.clearCart(userId).success }
	}


}