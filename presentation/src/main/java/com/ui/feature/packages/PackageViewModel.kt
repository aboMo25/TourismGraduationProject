package com.ui.feature.packages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Trip
import com.domain.usecase.GetAllTripsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PackageListViewModel(
	private val getAllTripsUseCase: GetAllTripsUseCase
) : ViewModel() {

	private val _uiState = MutableStateFlow<PackageListUiState>(PackageListUiState.Loading)
	val uiState: StateFlow<PackageListUiState> = _uiState

	init {
		loadDestinations()
	}

	fun loadDestinations() {
		viewModelScope.launch {
			try {
				val trips = getAllTripsUseCase()
				_uiState.value = PackageListUiState.Success(trips)
			} catch (e: Exception) {
				_uiState.value = PackageListUiState.Error("Failed to load trips")
			}
		}
	}
}

sealed class PackageListUiState {
	object Loading : PackageListUiState()
	data class Success(val trips: List<Trip>) : PackageListUiState()
	data class Error(val message: String) : PackageListUiState()
}

