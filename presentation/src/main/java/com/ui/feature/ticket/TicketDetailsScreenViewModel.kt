package com.ui.feature.ticket

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TourismSession
import com.domain.model.Ticket
import com.domain.network.ResultWrapper
// Removed unused import: import com.domain.model.CartItem2
// Removed unused import: import com.domain.repository.CheckoutRepository
// Removed unused import: import com.domain.usecase.GetCartItemsUseCase
import com.domain.usecase.GetTicketByIdUseCase // NEW: Import the new UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketDetailsScreenViewModel(
	private val tourismSession: TourismSession, // Keep if needed for other logic (e.g., user context)
	// Removed: private val getCartItemsUseCase: GetCartItemsUseCase,
	private val savedStateHandle: SavedStateHandle,
	private val getTicketByIdUseCase: GetTicketByIdUseCase // NEW: Inject the UseCase
	// Removed: private val checkoutRepository: CheckoutRepository
) : ViewModel() {

	private val _ticket = MutableStateFlow<Ticket?>(null)
	val ticket: StateFlow<Ticket?> = _ticket.asStateFlow()

	private val _uiState = MutableStateFlow<TicketDetailsUiState>(TicketDetailsUiState.Loading)
	val uiState: StateFlow<TicketDetailsUiState> = _uiState.asStateFlow()

	// Removed cart-related StateFlows as they don't belong here
	// private val _cartItems = MutableStateFlow<List<CartItem2>>(emptyList())
	// val cartItems: StateFlow<List<CartItem2>> = _cartItems.asStateFlow()
	// private val _loading = MutableStateFlow(false)
	// val loading: StateFlow<Boolean> = _loading.asStateFlow()
	// private val _error = MutableStateFlow<String?>(null)
	// val error: StateFlow<String?> = _error.asStateFlow()

	init {
		// Only keep the init block related to fetching ticket details
		// Retrieve ticketId from navigation arguments
		val bookingId: String? = savedStateHandle["bookingId"]
		Log.d("TicketDetailsViewModel", "Received bookingId: $bookingId")
		if (bookingId != null) {
			fetchTicketDetails(bookingId)
		} else {
			_uiState.value = TicketDetailsUiState.Error("Ticket ID not found in navigation arguments.")
			Log.e("TicketDetailsViewModel", "Booking ID is null. Cannot fetch ticket details.")
		}
	}

	// Removed loadCartItems as it doesn't belong in this ViewModel
	/*
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
	*/

	private fun fetchTicketDetails(bookingId: String) {
		_uiState.value = TicketDetailsUiState.Loading
		viewModelScope.launch {
			// NEW: Use the GetTicketByIdUseCase
			when (val result = getTicketByIdUseCase(bookingId)) {
				is ResultWrapper.Success -> {
					_ticket.value = result.value
					_uiState.value = TicketDetailsUiState.Success
					Log.d("TicketDetailsViewModel", "Ticket fetched successfully: ${result.value?.bookingId}")
				}
				is ResultWrapper.Failure -> {
					val errorMessage = result.exception.message ?: "Failed to load ticket details."
					_uiState.value = TicketDetailsUiState.Error(errorMessage)
					Log.e("TicketDetailsViewModel", "Error fetching ticket: $errorMessage", result.exception)
				}
			}
		}
	}

	fun retryFetchTicketDetails() {
		val bookingId: String? = savedStateHandle["bookingId"]
		if (bookingId != null) {
			fetchTicketDetails(bookingId)
		} else {
			_uiState.value = TicketDetailsUiState.Error("Ticket ID not found for retry.")
		}
	}
}

sealed class TicketDetailsUiState {
	object Loading : TicketDetailsUiState()
	object Success : TicketDetailsUiState()
	data class Error(val message: String) : TicketDetailsUiState()
}