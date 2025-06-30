package com.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Trip
import com.domain.usecase.GetAllTripsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
	private val getAllTripsUseCase: GetAllTripsUseCase,
) : ViewModel() {

	private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)
	val uiState = _uiState.asStateFlow()

	private val _trips = MutableStateFlow<List<Trip>>(emptyList())
	val trips = _trips.asStateFlow()

	private var hasLoadedTrips = false // Add this flag

	init {
		if (!hasLoadedTrips) {
			loadTrips()
		}
	}

	fun loadTrips() {
		viewModelScope.launch {
			_uiState.value = HomeScreenUIEvents.Loading
			try {
				val result = getAllTripsUseCase()
				_trips.value = result
				_uiState.value = HomeScreenUIEvents.Success(
//					featured = emptyList(), // Adjust as needed
//					popularProducts = emptyList(), // Adjust as needed
					categories = emptyList() // Adjust as needed
				)
				hasLoadedTrips = true
			} catch (e: Exception) {
				_uiState.value = HomeScreenUIEvents.Error(e.message ?: "Unknown error")
			}
		}
	}
}
sealed class HomeScreenUIEvents {
	data object Loading : HomeScreenUIEvents()
	data class Success(
		val categories: List<String>
	) : HomeScreenUIEvents()
	data class Error(val message: String) : HomeScreenUIEvents()
}