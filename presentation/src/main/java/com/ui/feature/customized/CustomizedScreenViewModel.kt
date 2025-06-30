package com.ui.feature.customized

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.CustomizedTripData
import com.domain.usecase.GetCustomizedTripUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomizedScreenViewModel(
	private val getCustomizedTripUseCase: GetCustomizedTripUseCase
):ViewModel() {

	private val _uiState = MutableStateFlow<CustomizedScreenUiState>(CustomizedScreenUiState.Loading)
	val uiState = _uiState.asStateFlow()

	init {
		loadCustomizedTripData()
	}

	private fun loadCustomizedTripData() {
		viewModelScope.launch {
			try {
				val cards = getCustomizedTripUseCase()
				_uiState.value = CustomizedScreenUiState.Success(cards)
			} catch (e: Exception) {
				_uiState.value = CustomizedScreenUiState.Error("Failed to load trips")
			}
		}
	}


}

sealed class CustomizedScreenUiState{
	data object Loading : CustomizedScreenUiState()
	data class Success(
		val customizedTripData: List<CustomizedTripData>
	) : CustomizedScreenUiState()
	data class Error(val message: String) : CustomizedScreenUiState()

}