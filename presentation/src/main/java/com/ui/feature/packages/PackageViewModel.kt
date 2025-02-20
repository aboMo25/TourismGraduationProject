package com.ui.feature.packages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.response.Destination
import com.ui.feature.splash.TourismData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PackageListViewModel : ViewModel() {
	private val _uiState = MutableStateFlow<PackageListUiState>(PackageListUiState.Loading)
	val uiState: StateFlow<PackageListUiState> = _uiState

	init {
		loadDestinations()
	}

	fun loadDestinations() {
		viewModelScope.launch {
			// Simulate loading
			delay(1000)
			_uiState.value = PackageListUiState.Success(TourismData.governorates)
		}
	}
}

sealed class PackageListUiState {
	object Loading : PackageListUiState()
	data class Success(val destinations: List<Destination>) : PackageListUiState()
	data class Error(val message: String) : PackageListUiState()
}
