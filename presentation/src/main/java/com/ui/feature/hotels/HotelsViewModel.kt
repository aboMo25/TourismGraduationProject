package com.ui.feature.hotels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Hotels // Assuming this is an older model or different
import com.domain.model.Hotels2 // Your domain model for cart items
import com.domain.usecase.AddHotelToCartUseCase // Use the specific API use case
import com.domain.usecase.GetAllHotelsUseCase
import com.TourismSession // Needed for userId
import com.domain.network.ResultWrapper // Needed to handle API results
import com.ui.feature.places.detailsOfPOI.AddToCartOperationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HotelsViewModel (
	private val getAllHotelsUseCase: GetAllHotelsUseCase,
	private val addHotelToCartUseCase: AddHotelToCartUseCase, // Use the API-specific use case
	private val tourismSession: TourismSession // Inject TourismSession to get userId
): ViewModel() {

	private val _currentHotel2 = MutableStateFlow<Hotels2?>(null)
	val currentHotel2: StateFlow<Hotels2?> = _currentHotel2.asStateFlow()

	private val _currentDetailsHotelScreenIndex = MutableStateFlow(0)
	val currentDetailsHotelScreenIndex: StateFlow<Int> = _currentDetailsHotelScreenIndex.asStateFlow()

	private val _uiState = MutableStateFlow<HotelsUiState>(HotelsUiState.Loading)
	val uiState: StateFlow<HotelsUiState> = _uiState

	// NEW: State for add to cart operation from this ViewModel
	private val _addToCartOperationState = MutableStateFlow<AddToCartOperationState>(AddToCartOperationState.Idle)
	val addToCartOperationState: StateFlow<AddToCartOperationState> = _addToCartOperationState.asStateFlow()

	init {
		loadHotels()
	}

	fun loadHotelDetails(hotel: Hotels2) {
		viewModelScope.launch {
			try {
				_currentHotel2.value = hotel
				_currentDetailsHotelScreenIndex.value = 0
				// No need to reload all hotels here, just set current.
				// _uiState.value = HotelsUiState.Success(getAllHotelsUseCase()) // This might be unnecessary here
				_uiState.value = HotelsUiState.Success(listOf(hotel)) // Or just transition to a DetailsSuccess state
			} catch (e: Exception) {
				_uiState.value = HotelsUiState.Error("Failed to load hotel details: ${e.message}")
			}
		}
	}

	fun loadHotels() {
		viewModelScope.launch {
			_uiState.value = HotelsUiState.Loading
			try {
				val hotels = getAllHotelsUseCase()
				_uiState.value = HotelsUiState.Success(hotels)
			} catch (e: Exception) {
				_uiState.value = HotelsUiState.Error("Failed to load hotels: ${e.message}")
			}
		}
	}

	fun addCurrentHotelToCart() { // Renamed from addCurrentHotelToCart2 for clarity
		val userId = tourismSession.getUserId()
		if (userId == null) {
			_addToCartOperationState.value = AddToCartOperationState.Error("User not logged in to add to cart.")
			return
		}

		viewModelScope.launch {
			_addToCartOperationState.value = AddToCartOperationState.Loading
			currentHotel2.value?.let { hotel ->
				when (val result = addHotelToCartUseCase(userId, hotel)) { // Pass userId and hotel
					is ResultWrapper.Success -> {
						_addToCartOperationState.value = AddToCartOperationState.Success("Hotel added to cart!")
						// Trigger a refresh of the main cart if CartViewModel2 subscribes to API updates
					}
					is ResultWrapper.Failure -> {
						_addToCartOperationState.value = AddToCartOperationState.Error(
							result.exception.message ?: "Failed to add hotel to cart."
						)
					}
				}
			} ?: run {
				_addToCartOperationState.value = AddToCartOperationState.Error("No hotel selected to add.")
			}
		}
	}

	// You might also want a method to reset the _addToCartOperationState
	fun resetAddToCartOperationState() {
		_addToCartOperationState.value = AddToCartOperationState.Idle
	}

	// Removed removeCurrentHotelToCart2 as it uses addItemToCartUseCase and is redundant/confusing here.
	// CartViewModel2 should handle remove operations via its dedicated functions.
}

sealed class HotelsUiState {
	object Loading : HotelsUiState()
	data class Success(val hotels: List<Hotels2>) : HotelsUiState()
	data class Error(val message: String) : HotelsUiState()
}

// Reuse AddToCartOperationState from DetailsOfPlaceScreenViewModel
// sealed class AddToCartOperationState { ... }