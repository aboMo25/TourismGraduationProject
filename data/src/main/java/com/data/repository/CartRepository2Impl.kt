//package com.data.repository
//
//import com.domain.model.AllPlaces
//import com.domain.model.CartItem
//import com.domain.model.Hotels
//import kotlinx.coroutines.sync.Mutex
//import kotlinx.coroutines.sync.withLock
//
//class CartRepository2Impl : CartRepository2 {
//
//	// Store cart items in memory
//	private val cartItems = mutableListOf<CartItem>()
//
//	// To prevent concurrent modification problems
//	private val mutex = Mutex()
//
//	override suspend fun getCartItems(): List<CartItem> {
//		return mutex.withLock {
//			cartItems.toList()  // Return a copy for safety
//		}
//	}
//
//	override suspend fun addPlaceToCart(place: AllPlaces) {
//		mutex.withLock {
//			cartItems.add(CartItem.PlaceItem(place))
//		}
//	}
//
//	override suspend fun addHotelToCart(hotel: Hotels) {
//		mutex.withLock {
//			cartItems.add(CartItem.HotelItem(hotel))
//		}
//	}
//
//	override suspend fun removeItemFromCart(item: CartItem) {
//		mutex.withLock {
//			cartItems.remove(item)
//		}
//	}
//}
