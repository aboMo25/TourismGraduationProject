package com.ui.feature.places.detailsOfPOI

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TourismSession
import com.domain.model.AllPlaces2
import com.domain.model.Review
import com.domain.network.ResultWrapper
import com.domain.usecase.AddPlaceToCartUseCase
import com.domain.usecase.GetMainPlacesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsOfPlaceScreenViewModel(
	private val getMainPlacesUseCase: GetMainPlacesUseCase,
	private val addPlaceToCartUseCase: AddPlaceToCartUseCase, // Use the API-specific use case
	private val tourismSession: TourismSession // Inject TourismSession to get userId
) : ViewModel() {

	private val _uiState = MutableStateFlow<DetailsOfPlaceScreenUiState>(DetailsOfPlaceScreenUiState.Loading)
	val uiState: StateFlow<DetailsOfPlaceScreenUiState> = _uiState

	// No longer need _currentPlace if AllPlaces2 is the primary domain model for cart/details
	private val _currentPlace2 = MutableStateFlow<AllPlaces2?>(null)
	val currentPlace2: StateFlow<AllPlaces2?> = _currentPlace2.asStateFlow()

	private val _reviewsList = MutableStateFlow<List<Review>>(emptyList())
	val reviewsList: StateFlow<List<Review>> = _reviewsList.asStateFlow()

	// NEW: State for add to cart operation from this ViewModel
	private val _addToCartOperationState = MutableStateFlow<AddToCartOperationState>(AddToCartOperationState.Idle)
	val addToCartOperationState: StateFlow<AddToCartOperationState> = _addToCartOperationState.asStateFlow()
	// Assuming you have UiEvent for Snackbars, if not, you'll need to define it

	private val _uiEvents = MutableSharedFlow<UiEvent>()
	val uiEvents = _uiEvents.asSharedFlow()

	sealed class UiEvent {
		data class ShowSnackbar(val message: String) : UiEvent()
		// Add other UI events as needed
	}

	fun addCurrentPlaceToCart() {
		Log.d("CART", "Attempting to add place to cart")
		val userId = tourismSession.getUserId() // Get userId from session
		if (userId == null) {
			Log.e("CART", "User not logged in. Cannot add to cart.")
			viewModelScope.launch {
				_uiEvents.emit(UiEvent.ShowSnackbar("Please log in to add items to your cart."))
			}
			_addToCartOperationState.value = AddToCartOperationState.Error("User not logged in.")
			return
		}
		val placeToAdd = _currentPlace2.value
		if (placeToAdd == null) {
			Log.e("CART", "No place details loaded. Cannot add to cart.")
			viewModelScope.launch {
				_uiEvents.emit(UiEvent.ShowSnackbar("No place selected to add to cart."))
			}
			_addToCartOperationState.value = AddToCartOperationState.Error("No place selected.")
			return
		}
		// Essential check: Ensure the place ID is not empty before proceeding
		if (placeToAdd.id.isEmpty()) {
			Log.e("CART", "Place ID is missing or empty for the current place. Cannot add to cart.")
			viewModelScope.launch {
				_uiEvents.emit(UiEvent.ShowSnackbar("Error: Place ID is missing for this item."))
			}
			_addToCartOperationState.value = AddToCartOperationState.Error("Place ID is missing.")
			return
		}
		viewModelScope.launch {
			Log.d("CART", "Starting coroutine to add place with ID: ${placeToAdd.id} for User ID: $userId")
			_addToCartOperationState.value = AddToCartOperationState.Loading

			when (val result = addPlaceToCartUseCase(userId.toString(), placeToAdd)) {
				is ResultWrapper.Success -> {
					Log.d("CART", "Successfully added place to cart")
					_addToCartOperationState.value = AddToCartOperationState.Success("Place added to cart!")
					_uiEvents.emit(UiEvent.ShowSnackbar("Place added to cart successfully!")) // Show success to user
				}
				is ResultWrapper.Failure -> {
					val errorMessage = result.exception.message ?: "Failed to add place to cart due to an unknown error."
					Log.e("CART", "Failed to add place: $errorMessage", result.exception)
					_addToCartOperationState.value = AddToCartOperationState.Error(errorMessage)
					_uiEvents.emit(UiEvent.ShowSnackbar(errorMessage)) // Show error to user
				}
			}
		}
	}

	// You might also want a method to reset the _addToCartOperationState
	fun resetAddToCartOperationState() {
		_addToCartOperationState.value = AddToCartOperationState.Idle
	}

	fun loadPlaceDetails(placeId: String) {
		viewModelScope.launch {
			_uiState.value = DetailsOfPlaceScreenUiState.Loading
			try {

				val mainPlaces = getMainPlacesUseCase()
				Log.d("Main", " ${mainPlaces}")
				val place = mainPlaces.flatMap { it.allPlaces }
					// OLD: .find { it.placeData.id.toString() == placeId }
					// NEW: Safely access id and compare
					.find { it.placeData.id.toString() == placeId } //
				if (place != null) {
					_currentPlace2.value = place
					_uiState.value = DetailsOfPlaceScreenUiState.Success
				} else {
					_uiState.value = DetailsOfPlaceScreenUiState.Error("Place not found")
				}
			} catch (e: Exception) {
				_uiState.value =
					DetailsOfPlaceScreenUiState.Error("Failed to load place details: ${e.message}")
			}
		}
	}
}

sealed class DetailsOfPlaceScreenUiState {
	object Loading : DetailsOfPlaceScreenUiState()
	object Success : DetailsOfPlaceScreenUiState()
	data class Error(val message: String) : DetailsOfPlaceScreenUiState()
}

// NEW: For Add to Cart operation status
sealed class AddToCartOperationState {
	object Idle : AddToCartOperationState()
	object Loading : AddToCartOperationState()
	data class Success(val message: String) : AddToCartOperationState()
	data class Error(val message: String) : AddToCartOperationState()
}
