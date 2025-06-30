package com.ui.feature.places.governates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.MainPlace2
import com.domain.usecase.GetMainPlacesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlacesOfInterestViewModel(
	private val getMainPlacesUseCase: GetMainPlacesUseCase
): ViewModel() {
	private val _uiState = MutableStateFlow<PlacesOfInterestUiState>(PlacesOfInterestUiState.Loading)
	val uiState: StateFlow<PlacesOfInterestUiState> = _uiState

	init {
		loadDestinations()
	}
	private fun loadDestinations() {
		viewModelScope.launch {
			try {
				val mainPlaces = getMainPlacesUseCase()
				_uiState.value = PlacesOfInterestUiState.Success(mainPlaces)
			} catch (e: Exception) {
				_uiState.value = PlacesOfInterestUiState.Error("Failed to load trips")
			}
		}
	}
}

sealed class PlacesOfInterestUiState {
	object Loading : PlacesOfInterestUiState()
//	data class Success(val mainPlace: List<MainPlace>) : PlacesOfInterestUiState()
	data class Success(val mainPlace: List<MainPlace2>) : PlacesOfInterestUiState()
	data class Error(val message: String) : PlacesOfInterestUiState()
}