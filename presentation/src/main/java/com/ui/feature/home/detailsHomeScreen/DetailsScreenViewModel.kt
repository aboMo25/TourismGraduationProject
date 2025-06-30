package com.ui.feature.home.detailsHomeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TourismSession
import com.domain.model.Hotels
import com.domain.model.Hotels2
import com.domain.model.Review
import com.domain.model.Trip
import com.domain.network.ResultWrapper
import com.domain.usecase.AddTripToCartUseCase
import com.domain.usecase.GetAllHotelsUseCase
import com.domain.usecase.GetAllTripsUseCase
import com.ui.feature.places.detailsOfPOI.DetailsOfPlaceScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
	private val getTripUseCase: GetAllTripsUseCase,
	private val getAllHotelsUseCase: GetAllHotelsUseCase,
	private val addTripToCartUseCase: AddTripToCartUseCase,
	private val tourismSession: TourismSession, // NEW: Inject TourismSession
	) : ViewModel() {

	private val _uiState = MutableStateFlow<DetailsScreenUiState>(DetailsScreenUiState.Loading)
	val uiState: StateFlow<DetailsScreenUiState> = _uiState

	private val _currentTrip = MutableStateFlow<Trip?>(null)
	val currentTrip: StateFlow<Trip?> = _currentTrip.asStateFlow()

	private val _hotelsList = MutableStateFlow<List<Hotels2>>(emptyList())
	val hotelsList: StateFlow<List<Hotels2>> = _hotelsList.asStateFlow()

	private val _currentDetailsScreenIndex = MutableStateFlow(0)
	val currentDetailsScreenIndex: StateFlow<Int> = _currentDetailsScreenIndex.asStateFlow()

	// Add a MutableSharedFlow for UI messages (like toasts)
	private val _userMessage = MutableStateFlow<String?>(null)
	val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

	fun loadTrip(tripId: String) {
		viewModelScope.launch {
			_uiState.value = DetailsScreenUiState.Loading
			try {
				val trips = getTripUseCase()
				val trip = trips.firstOrNull { it.id.toString() == tripId } // ✅ Search by id
				if (trip != null) {
					_currentTrip.value = trip
					_uiState.value = DetailsScreenUiState.Success
				} else {
					_uiState.value = DetailsScreenUiState.Error("Trip not found!")
				}
			} catch (e: Exception) {
				_uiState.value = DetailsScreenUiState.Error("Failed to load trip: ${e.message}")
			}
		}
	}
	fun loadHotels() {
		viewModelScope.launch {
			try {
				val hotels = getAllHotelsUseCase()
				_hotelsList.value = hotels
			} catch (e: Exception) {
				// You might want to update uiState here too if needed
			}
		}
	}

	fun addCurrentTripToCart() { // userId is now taken from tourismSession
		val trip = _currentTrip.value
		if (trip == null) {
			_userMessage.value = "Error: No trip selected to add to cart."
			return
		}
		val userId = tourismSession.getUserId() // Get userId from the injected session
		if (userId == null) {
			_userMessage.value = "User not logged in. Cannot add trip to cart."
			return
		}
		viewModelScope.launch {
			when (val result = addTripToCartUseCase(userId.toString(), trip)) {
				is ResultWrapper.Success -> {
					if (result.value) {
						_userMessage.value = "Trip added to cart successfully!"
					} else {
						_userMessage.value = "Failed to add trip to cart."
					}
				}
				is ResultWrapper.Failure -> {
					val errorMessage = result.exception?.message ?: "An unknown error occurred."
					_userMessage.value = "Error adding trip to cart: $errorMessage"
					Log.e("DetailsScreenViewModel", "Error adding trip to cart: $errorMessage", result.exception)
				}
			}
			//_userMessage.value = null // Clear message after it's been consumed, or handle it in UI
		}
	}


	fun onNextClicked() {
		val currentDestination = _currentTrip.value
		if (currentDestination != null && _currentDetailsScreenIndex.value < currentDestination.detailsScreenData.lastIndex) {
			_currentDetailsScreenIndex.value++
		}
	}
	fun onBackClicked() {
		if (_currentDetailsScreenIndex.value > 0) {
			_currentDetailsScreenIndex.value--
		}
	}

}

sealed class DetailsScreenUiState {
	object Loading : DetailsScreenUiState()
	object Success : DetailsScreenUiState()
	data class Error(val message: String) : DetailsScreenUiState()
}