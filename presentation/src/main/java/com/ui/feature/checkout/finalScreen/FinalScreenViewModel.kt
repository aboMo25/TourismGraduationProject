package com.ui.feature.checkout.finalScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FinalScreenViewModel:ViewModel() {
	private val _uiState= MutableStateFlow<FinalScreenUiState>(FinalScreenUiState.Loading)
	val uiState: StateFlow<FinalScreenUiState> = _uiState

    fun onButtonClick(message: String) {
        _uiState.value= FinalScreenUiState.Success(message)
    }

}

sealed class FinalScreenUiState{
	object Loading: FinalScreenUiState()
	data class Success(val message: String): FinalScreenUiState()
	data class Error(val message: String): FinalScreenUiState()
}