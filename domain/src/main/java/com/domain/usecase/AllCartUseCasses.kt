//package com.domain.usecase
//
//import com.domain.model.AllPlaces2
//import com.domain.model.CartItem2
//import com.domain.model.Hotels2
//import com.domain.network.ResultWrapper
//import com.domain.repository.CartRepository3 // Your correct repository
//// Removed unused imports: CartItem, CartRepository, CartRepository2, Flow
//
//// AddItemToCartUseCase needs to be specialized or handle polymorphism
//// Since your API has addPlaceToCart and addHotelToCart, it's cleaner to have separate use cases.
//// If you truly need a single AddItemToCartUseCase, it must handle the type branching.
//
//// Option A: Keep separate AddPlaceToCartUseCase and AddHotelToCartUseCase for clarity
//// (This is what we had previously and is generally recommended for distinct API calls)
//class AddPlaceToCartUseCase(private val cartRepository: CartRepository3) {
//	suspend operator fun invoke(userId: Int, place: AllPlaces2): ResultWrapper<Boolean> {
//		return cartRepository.addPlaceToCart(userId, place)
//	}
//}
//
//class AddHotelToCartUseCase(private val cartRepository: CartRepository3) {
//	suspend operator fun invoke(userId: Int, hotel: Hotels2): ResultWrapper<Boolean> {
//		return cartRepository.addHotelToCart(userId, hotel)
//	}
//}
//
//// Option B: A single AddItemToCartUseCase that branches internally (more complex, less direct for API)
//// If you insist on this single use case, it would look like this:
//class AddItemToCartUseCase(private val cartRepository: CartRepository3) {
//	suspend operator fun invoke(userId: Int, item: CartItem2): ResultWrapper<Boolean> {
//		return when (item) {
//			is AllPlaces2 -> cartRepository.addPlaceToCart(userId, item)
//			is Hotels2 -> cartRepository.addHotelToCart(userId, item)
//			// Add other types if necessary
//			else -> ResultWrapper.Failure(IllegalArgumentException("Unsupported CartItem type for adding."))
//		}
//	}
//}
//
//
//// Corrected Use Cases: All now take userId
//class RemoveItemFromCartUseCase2(private val cartRepository: CartRepository3) {
//	suspend operator fun invoke(userId: Int, itemId: String): ResultWrapper<Boolean> { // Added userId
//		return cartRepository.removeItem(userId, itemId)
//	}
//}
//
//class GetCartItemsUseCase2(private val cartRepository: CartRepository3) {
//	suspend operator fun invoke(userId: Int): ResultWrapper<List<CartItem2>> { // Changed to operator fun
//		return cartRepository.getCartItems(userId)
//	}
//}
//
//class ClearCartUseCase(private val cartRepository: CartRepository3) {
//	suspend operator fun invoke(userId: Int): ResultWrapper<Boolean> { // Added userId
//		return cartRepository.clearCart(userId)
//	}
//}
//
//class UpdateItemQuantityUseCase(private val cartRepository: CartRepository3) {
//	suspend operator fun invoke(userId: Int, itemId: String, newQuantity: Int): ResultWrapper<Boolean> { // Added userId
//		return cartRepository.updateItemQuantity(userId, itemId, newQuantity)
//	}
//}