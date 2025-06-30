package com.ui.feature.checkout.payment_method_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.network.ResultWrapper
import com.domain.usecase.CreateOrderUseCase // Import
import com.domain.model.Ticket // Import
import com.domain.model.CartItem2
import com.domain.model.PassengerDetails
import com.domain.model.request.CreateOrderRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentMethodScreenViewModel(
	private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

	private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
	val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()


	// Data passed from previous screens (or could be fetched again if necessary)
	// In a real app, you might use SavedStateHandle or a shared ViewModel scope
	// For simplicity, we'll assume these are passed or retrieved
	var cartItems: List<CartItem2> = emptyList()
	var passengerDetails: PassengerDetails? = null
	var finalAmount: Double = 0.0
	var tripStartDate : String = "15/10/2023" // Default date, replace with actual logic
	var tripEndDate : String = "21/10/2023"

	fun proceedToCheckout(selectedPaymentMethodId: String, voucherCode: String?) {
		val currentPassengerDetails = passengerDetails
		val currentCartItems = cartItems
		val currentFinalAmount = finalAmount
		val currentTripStartDate = tripStartDate
		val currentTripEndDate = tripEndDate


		if (currentPassengerDetails == null || currentCartItems.isEmpty() || currentFinalAmount <= 0) {
			_paymentState.value = PaymentState.Error("Missing order details. Please go back and review your selection.")
			return
		}

		_paymentState.value = PaymentState.Loading

		val request = CreateOrderRequest(
			userId = currentPassengerDetails.fullName, // Using full name as userId placeholder, replace with actual userId from TourismSession
			cartItems = currentCartItems,
			passengerDetails = currentPassengerDetails,
			selectedPaymentMethod = selectedPaymentMethodId,
			voucherCode = voucherCode,
			finalAmount = currentFinalAmount,
			tripStartDate = currentTripStartDate, // Replace with actual logic to get trip start date
			tripEndDate = currentTripEndDate // Replace with actual logic to get trip end date
		)

		viewModelScope.launch {
			when (val result = createOrderUseCase(request)) {
				is ResultWrapper.Success -> {
					_paymentState.value = PaymentState.Success(result.value, "Payment successful!")
				}
				is ResultWrapper.Failure -> {
					_paymentState.value = PaymentState.Error(
						result.exception.message ?: "Payment failed. Please try again."
					)
				}
			}
		}
	}

	fun resetPaymentState() {
		_paymentState.value = PaymentState.Idle
	}
}

sealed class PaymentState {
	object Idle : PaymentState()
	object Loading : PaymentState()
	data class Success(val ticket: Ticket, val message: String) : PaymentState()
	data class Error(val message: String) : PaymentState()
}