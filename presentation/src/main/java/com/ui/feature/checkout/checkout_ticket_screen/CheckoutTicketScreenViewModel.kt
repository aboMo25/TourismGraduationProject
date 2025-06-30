package com.ui.feature.checkout.checkout_ticket_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TourismSession
import com.domain.model.CartItem2
import com.domain.model.PassengerDetails
import com.domain.model.Ticket
import com.domain.model.request.CreateOrderRequest
import com.domain.network.ResultWrapper
import com.domain.usecase.CreateOrderUseCase
import com.domain.usecase.GetCartItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutTicketScreenViewModel(
	private val tourismSession: TourismSession,
	private val getCartItemsUseCase: GetCartItemsUseCase,
	private val createOrderUseCase: CreateOrderUseCase // Inject the new use case
) : ViewModel() {

	private val _cartItems = MutableStateFlow<List<CartItem2>>(emptyList())
	val cartItems: StateFlow<List<CartItem2>> = _cartItems.asStateFlow()

	private val _originalCartTotal = MutableStateFlow(0.0) // Total before discounts
	val originalCartTotal: StateFlow<Double> = _originalCartTotal.asStateFlow()

	private val _discountAmount = MutableStateFlow(0.0)
	val discountAmount: StateFlow<Double> = _discountAmount.asStateFlow()

	private val _tripStartDate = MutableStateFlow("15/10/2023") // Default date, replace with actual logic
	val tripStartDate: StateFlow<String> = _tripStartDate.asStateFlow()

	private val _tripEndDate = MutableStateFlow("21/10/2023")
	val tripEndDate: StateFlow<String> = _tripEndDate.asStateFlow()

	private val _finalPrice = MutableStateFlow(0.0)
	val finalPrice: StateFlow<Double> = _finalPrice.asStateFlow()

	// Passenger details collected in this screen
	private val _fullName = MutableStateFlow("")
	val fullName: StateFlow<String> = _fullName.asStateFlow()

	private val _email = MutableStateFlow("")
	val email: StateFlow<String> = _email.asStateFlow()

	private val _adultPassengers = MutableStateFlow(1) // Default to 1
	val adultPassengers: StateFlow<Int> = _adultPassengers.asStateFlow()

	private val _youngPassengers = MutableStateFlow(0) // Default to 0
	val youngPassengers: StateFlow<Int> = _youngPassengers.asStateFlow()

	private val _nationality = MutableStateFlow("Egyptian") // Default to 0
	val nationality: StateFlow<String> = _nationality.asStateFlow()

	private val _phoneNumber = MutableStateFlow("01003272476") // Default to 0
	val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

	private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
	val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

	init {
		fetchCartItemsAndCalculateTotals()
		// Load existing user info if available (e.g., from TourismSession or user profile)
		tourismSession.getUserName()?.let { _fullName.value = it }
		tourismSession.getUserEmail()?.let { _email.value = it }
	}

	private fun fetchCartItemsAndCalculateTotals() {
		val userId = tourismSession.getUserId()
		if (userId == null) {
			_checkoutState.value = CheckoutState.Error("User not logged in.")
			return
		}

		_checkoutState.value = CheckoutState.Loading
		viewModelScope.launch {
			when (val result = getCartItemsUseCase(userId)) {
				is ResultWrapper.Success -> {
					_cartItems.value = result.value
					val subtotal = result.value.sumOf { (it.price ?: 0.0)  }
					_originalCartTotal.value = subtotal
					calculateFinalPrice() // Calculate discount and final price
					_checkoutState.value = CheckoutState.Success("Cart items loaded.")
				}
				is ResultWrapper.Failure -> {
					_checkoutState.value = CheckoutState.Error(
						result.exception.message ?: "Failed to load cart items for checkout."
					)
					_cartItems.value = emptyList()
					_originalCartTotal.value = 0.0
					_discountAmount.value = 0.0
					_finalPrice.value = 0.0
				}
			}
		}
	}

	// --- User Input Handlers ---
	fun onFullNameChange(name: String) { _fullName.value = name }
	fun onEmailChange(email: String) { _email.value = email }
	fun onAdultPassengersChange(count: String) {
		val newCount = count.toIntOrNull() ?: 0
		_adultPassengers.value = newCount.coerceAtLeast(0) // Prevent negative
		calculateFinalPrice()
	}
	fun onYoungPassengersChange(count: String) {
		val newCount = count.toIntOrNull() ?: 0
		_youngPassengers.value = newCount.coerceAtLeast(0) // Prevent negative
		calculateFinalPrice()
	}

	// --- Discount Logic ---
	private fun calculateFinalPrice() {
		val currentTotal = _originalCartTotal.value
		val youngPassengersCount = _youngPassengers.value

		var discount = 0.0
		// Define your discount logic clearly:
		// Example: If there's at least one young passenger, apply 50% discount on their portion.
		// This is a simplification. A more robust logic might need to know which items are for young passengers.
		// For now, let's assume if there are young passengers, a flat 50% discount on a *portion* of the total.
		// A common approach is a percentage off the *total* if specific criteria are met.
		if (youngPassengersCount > 0) {
			// Let's say, 50% off the total amount IF there's at least one young passenger.
			// Adjust this business logic as per your requirements.
			discount = currentTotal * 0.50
			// Or, if discount is per young passenger on their 'share':
			// Assume average cost per person is currentTotal / (adultPassengers + youngPassengers)
			// Then discount = (currentTotal / totalPassengers) * youngPassengers * 0.5
			// For simplicity, let's just make it 50% of the total if young passengers exist.
		}

		_discountAmount.value = discount
		_finalPrice.value = (currentTotal - discount).coerceAtLeast(0.0) // Price cannot be negative
	}

	// --- Checkout Action ---
	fun proceedToCheckout(selectedPaymentMethodId: String, voucherCode: String?) {
		val userId = tourismSession.getUserId()
		if (userId == null) {
			_checkoutState.value = CheckoutState.Error("User not logged in to proceed checkout.")
			return
		}
		if (_cartItems.value.isEmpty()) {
			_checkoutState.value = CheckoutState.Error("Cart is empty. Cannot proceed to checkout.")
			return
		}

		_checkoutState.value = CheckoutState.Loading

		val passengerDetails = PassengerDetails(
			fullName = _fullName.value,
			email = _email.value,
			totalAdultPassengers = _adultPassengers.value,
			totalYoungPassengers = _youngPassengers.value,
			nationality = _nationality.value,
			phoneNumber = _phoneNumber.value
		)

		val request = CreateOrderRequest(
			userId = userId.toString(),
			cartItems = _cartItems.value, // Send the actual cart items
			passengerDetails = passengerDetails,
			selectedPaymentMethod = selectedPaymentMethodId,
			voucherCode = voucherCode,
			finalAmount = _finalPrice.value,
			tripStartDate = _tripStartDate.value, // Replace with actual date logic,
			tripEndDate = _tripEndDate.value// Replace with actual date logic
		)

		viewModelScope.launch {
			when (val result = createOrderUseCase(request)) {
				is ResultWrapper.Success -> {
					// Successfully created order, now we have the Ticket details
					_checkoutState.value = CheckoutState.SuccessWithData(result.value, "Order created successfully!")
					// The next screen (PaymentMethodScreen) will handle navigation to Final/TicketDetails
				}
				is ResultWrapper.Failure -> {
					_checkoutState.value = CheckoutState.Error(
						result.exception.message ?: "Failed to create order."
					)
				}
			}
		}
	}

	fun resetCheckoutState() {
		_checkoutState.value = CheckoutState.Idle
	}
}

// Define specific states for checkout process
sealed class CheckoutState {
	object Idle : CheckoutState()
	object Loading : CheckoutState()
	data class Success(val message: String) : CheckoutState() // For initial cart load success
	data class SuccessWithData(val ticket: Ticket, val message: String) : CheckoutState() // For checkout success
	data class Error(val message: String) : CheckoutState()
}