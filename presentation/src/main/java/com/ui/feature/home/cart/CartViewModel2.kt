package com.ui.feature.cart

import android.os.Build
import android.util.Log // Import Log for debugging
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TourismSession
import com.domain.model.AllPlaces2
import com.domain.model.CartItem2
import com.domain.model.Hotels2
import com.domain.model.PassengerDetails // Import PassengerDetails
import com.domain.model.request.CreateOrderRequest
import com.domain.network.ResultWrapper
import com.domain.usecase.AddHotelToCartUseCase
import com.domain.usecase.AddPlaceToCartUseCase
import com.domain.usecase.ClearCartUseCase
import com.domain.usecase.GetCartItemsUseCase
import com.domain.usecase.RemoveItemFromCartUseCase2
import com.domain.usecase.UpdateItemQuantityUseCase
import com.domain.repository.CheckoutRepository // Import CheckoutRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate // For LocalDate
import java.time.format.DateTimeFormatter // For formatting dates

class CartViewModel2(
	private val tourismSession: TourismSession,
	private val getCartItemsUseCase: GetCartItemsUseCase,
	private val addPlaceToCartUseCase: AddPlaceToCartUseCase,
	private val addHotelToCartUseCase: AddHotelToCartUseCase,
	private val removeItemFromCartUseCase: RemoveItemFromCartUseCase2,
	private val updateItemQuantityUseCase: UpdateItemQuantityUseCase,
	private val clearCartUseCase: ClearCartUseCase,
	private val checkoutRepository: CheckoutRepository // Inject CheckoutRepository
) : ViewModel() {

	private val _cartItems = MutableStateFlow<List<CartItem2>>(emptyList())
	val cartItems: StateFlow<List<CartItem2>> = _cartItems.asStateFlow()

	private val _loading = MutableStateFlow(false)
	val loading: StateFlow<Boolean> = _loading.asStateFlow()

	private val _error = MutableStateFlow<String?>(null)
	val error: StateFlow<String?> = _error.asStateFlow()

	// Event for navigation (e.g., to TicketDetailsScreen)
	private val _navigateToTicketDetails = MutableSharedFlow<String>()
	val navigateToTicketDetails: SharedFlow<String> = _navigateToTicketDetails.asSharedFlow()

	// Event for showing a success/error message (e.g., Toast/Snackbar)
	private val _userMessage = MutableSharedFlow<String>()
	val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

	init {
		loadCartItems()
	}

	fun loadCartItems() {
		val userId = tourismSession.getUserId()
		if (userId == null) {
			_error.value = "User not logged in."
			_cartItems.value = emptyList()
			return
		}
		viewModelScope.launch {
			_loading.value = true
			_error.value = null
			when (val result = getCartItemsUseCase(userId)) {
				is ResultWrapper.Success -> {
					_cartItems.value = result.value
					Log.d("CartViewModel2", "Cart items loaded: ${_cartItems.value.size}")
				}

				is ResultWrapper.Failure -> {
					val errorMessage = result.exception?.message ?: "Failed to load cart items."
					_error.value = errorMessage
					_cartItems.value = emptyList() // Clear cart on error
					Log.e("CartViewModel2", "Error loading cart items: $errorMessage", result.exception)
				}
			}
			_loading.value = false
		}
	}

	fun addPlaceToCart(place: AllPlaces2) {
		val userId = tourismSession.getUserId()
		if (userId == null) {
			_error.value = "User not logged in."
			viewModelScope.launch { _userMessage.emit("Error: User not logged in.") }
			return
		}
		viewModelScope.launch {
			_loading.value = true
			_error.value = null
			when (val result = addPlaceToCartUseCase(userId.toString(), place)) {
				is ResultWrapper.Success -> {
					if (result.value) { // Assuming result.value is true for success
						loadCartItems() // Reload cart to reflect changes
						_userMessage.emit("Place added to cart!")
					} else {
						val msg = "Failed to add place to cart (API response indicated failure)."
						_error.value = msg
						_userMessage.emit(msg)
					}
				}

				is ResultWrapper.Failure -> {
					val msg = result.exception?.message ?: "Failed to add place to cart."
					_error.value = msg
					_userMessage.emit(msg)
					Log.e("CartViewModel2", "Error adding place to cart: $msg", result.exception)
				}
			}
			_loading.value = false
		}
	}

	fun addHotelToCart(hotel: Hotels2) {
		val userId = tourismSession.getUserId()
		if (userId == null) {
			_error.value = "User not logged in."
			viewModelScope.launch { _userMessage.emit("Error: User not logged in.") }
			return
		}
		viewModelScope.launch {
			_loading.value = true
			_error.value = null
			when (val result = addHotelToCartUseCase(userId, hotel)) {
				is ResultWrapper.Success -> {
					if (result.value) { // Assuming result.value is true for success
						loadCartItems() // Reload cart to reflect changes
						_userMessage.emit("Hotel added to cart!")
					} else {
						val msg = "Failed to add hotel to cart (API response indicated failure)."
						_error.value = msg
						_userMessage.emit(msg)
					}
				}

				is ResultWrapper.Failure -> {
					val msg = result.exception?.message ?: "Failed to add hotel to cart."
					_error.value = msg
					_userMessage.emit(msg)
					Log.e("CartViewModel2", "Error adding hotel to cart: $msg", result.exception)
				}
			}
			_loading.value = false
		}
	}

	fun removeItemFromCart(itemId: String) {
		val userId = tourismSession.getUserId()
		if (userId == null) {
			_error.value = "User not logged in."
			viewModelScope.launch { _userMessage.emit("Error: User not logged in.") }
			return
		}
		viewModelScope.launch {
			_loading.value = true
			_error.value = null
			when (val result = removeItemFromCartUseCase(userId, itemId)) {
				is ResultWrapper.Success -> {
					loadCartItems() // Reload cart
					_userMessage.emit("Item removed from cart.")
				}
				is ResultWrapper.Failure -> {
					val msg = result.exception?.message ?: "Failed to remove item from cart."
					_error.value = msg
					_userMessage.emit(msg)
					Log.e("CartViewModel2", "Error removing item from cart: $msg", result.exception)
				}
			}
			_loading.value = false
		}
	}

	fun updateItemQuantity(itemId: String, newQuantity: Int) {
		val userId = tourismSession.getUserId()
		if (userId == null) {
			_error.value = "User not logged in."
			viewModelScope.launch { _userMessage.emit("Error: User not logged in.") }
			return
		}
		viewModelScope.launch {
			_loading.value = true
			_error.value = null
			when (val result = updateItemQuantityUseCase(userId, itemId, newQuantity)) {
				is ResultWrapper.Success -> {
					if (result.value) {
						loadCartItems() // Reload cart
						_userMessage.emit("Quantity updated.")
					} else {
						val msg = "Failed to update item quantity (API response indicated failure)."
						_error.value = msg
						_userMessage.emit(msg)
					}
				}

				is ResultWrapper.Failure -> {
					val msg = result.exception?.message ?: "Failed to update item quantity."
					_error.value = msg
					_userMessage.emit(msg)
					Log.e("CartViewModel2", "Error updating quantity: $msg", result.exception)
				}
			}
			_loading.value = false
		}
	}

	fun clearCart() {
		val userId = tourismSession.getUserId()
		if (userId == null) {
			_error.value = "User not logged in."
			viewModelScope.launch { _userMessage.emit("Error: User not logged in.") }
			return
		}
		viewModelScope.launch {
			_loading.value = true
			_error.value = null
			when (val result = clearCartUseCase(userId)) {
				is ResultWrapper.Success -> {
					if (result.value) {
						loadCartItems() // Reload cart (should be empty)
						_userMessage.emit("Cart cleared successfully!")
					} else {
						val msg = "Failed to clear cart (API response indicated failure)."
						_error.value = msg
						_userMessage.emit(msg)
					}
				}

				is ResultWrapper.Failure -> {
					val msg = result.exception?.message ?: "Failed to clear cart."
					_error.value = msg
					_userMessage.emit(msg)
					Log.e("CartViewModel2", "Error clearing cart: $msg", result.exception)
				}
			}
			_loading.value = false
		}
	}

	// NEW FUNCTION: Handle the checkout process
	@RequiresApi(Build.VERSION_CODES.O)
	fun proceedToCheckout() {
		val userId = tourismSession.getUserId()
		if (userId == null) {
			_error.value = "User not logged in."
			viewModelScope.launch { _userMessage.emit("Checkout Error: User not logged in.") }
			return
		}
		if (_cartItems.value.isEmpty()) {
			viewModelScope.launch { _userMessage.emit("Your cart is empty. Add items before checking out.") }
			return
		}

		viewModelScope.launch {
			_loading.value = true
			_error.value = null

			// --- IMPORTANT: Collect Passenger Details, Payment Method, and Trip Dates ---
			// For simplicity, I'm using placeholders here. In a real app, you'd
			// have a UI flow to get this from the user.
			val passengerDetails = PassengerDetails(
				fullName = "", // Replace with actual user input
				email = "", // Replace with actual user input
				phoneNumber = "", // Replace with actual user input
				nationality = "Egyptian", // Replace with actual user input
				totalAdultPassengers = 2,
				totalYoungPassengers = 1

			)

			val selectedPaymentMethod = "Credit Card" // Replace with actual user selection
			val finalAmount = _cartItems.value.sumOf { it.price ?: 0.0 } // Calculate total price

			// Determine trip start/end dates. This is a common point of complexity.
			// Option 1: User explicitly selects dates for the whole trip.
			// Option 2: Dates are derived from the items (e.g., earliest start date, latest end date).
			// For simplicity, using hardcoded dates or deriving a generic range.
			val tripStartDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE) // Example: 7 days from now
			val tripEndDate = LocalDate.now().plusDays(14).format(DateTimeFormatter.ISO_LOCAL_DATE) // Example: 14 days from now

			// You might need a more sophisticated way to get trip dates,
			// e.g., from a Trip object if the whole cart is for one specific trip booking,
			// or from user input in a previous screen.

			val createOrderRequest = CreateOrderRequest(
				userId = userId.toString(),
				cartItems = _cartItems.value,
				passengerDetails = passengerDetails,
				selectedPaymentMethod = selectedPaymentMethod,
				voucherCode = null, // Or collect from UI
				finalAmount = finalAmount,
				tripStartDate = tripStartDate,
				tripEndDate = tripEndDate
			)

			when (val result = checkoutRepository.createOrder(createOrderRequest)) {
				is ResultWrapper.Success -> {
					val ticket = result.value
					Log.d("CartViewModel2", "Order created successfully! Booking ID: ${ticket.bookingId}")
					_userMessage.emit("Order placed successfully! Booking ID: ${ticket.bookingId}")
					clearCart() // Clear the cart after successful checkout

					// Navigate to the ticket details screen
					_navigateToTicketDetails.emit(ticket.bookingId)
				}
				is ResultWrapper.Failure -> {
					val msg = result.exception?.message ?: "Failed to create order."
					_error.value = msg
					_userMessage.emit("Checkout Error: $msg")
					Log.e("CartViewModel2", "Error creating order: $msg", result.exception)
				}

			}
			_loading.value = false
		}
	}
}

// Consider adding specific event classes if you have many types of navigation/messages
// sealed class CartEvent {
//    data class NavigateToTicketDetails(val bookingId: String) : CartEvent()
//    data class ShowUserMessage(val message: String) : CartEvent()
// }