package com.ui.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.response.Destination
import com.ui.feature.splash.DetailsScreenData
import com.ui.feature.splash.TourismData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsScreenViewModel : ViewModel() {

	private val _detailsScreen = MutableStateFlow<DetailsScreenData>(TourismData.detailsScreen.first())
	val currentDetailsScreen: StateFlow<DetailsScreenData> = _detailsScreen.asStateFlow()

	private val _currentIndex = MutableStateFlow(0)
	val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()



	private val _uiState = MutableStateFlow<DetailsScreenUiState>(DetailsScreenUiState.Loading)
	val uiState: StateFlow<DetailsScreenUiState> = _uiState
	init {
		loadDetails()
	}
	fun onNextClicked() {
		if (_currentIndex.value < TourismData.detailsScreen.lastIndex) {
			_currentIndex.value++
			_detailsScreen.value = TourismData.detailsScreen[_currentIndex.value]
		}
	}
	fun onBackClicked() {
		if (_currentIndex.value > 0) {
            _currentIndex.value--
            _detailsScreen.value = TourismData.detailsScreen[_currentIndex.value]
        }
	}



	fun loadDetails() {
		viewModelScope.launch {
			// Simulate loading
			delay(1000)
			_uiState.value = DetailsScreenUiState.Success(TourismData.governorates)
		}
	}

}

sealed class DetailsScreenUiState {
	object Loading : DetailsScreenUiState()
	data class Success(val destinations: List<Destination>) : DetailsScreenUiState()
	data class Error(val message: String) : DetailsScreenUiState()
}