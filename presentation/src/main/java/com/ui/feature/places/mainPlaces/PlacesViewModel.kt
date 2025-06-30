package com.ui.feature.places.mainPlaces

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.AllPlaces
import com.domain.model.AllPlaces2
import com.domain.usecase.GetMainPlacesUseCase
import kotlinx.coroutines.launch
class PlacesViewModel(
	private val getMainPlacesUseCase: GetMainPlacesUseCase
) : ViewModel() {

	var uiState by mutableStateOf(PlacesUiState())
		private set

	fun loadPlacesFor(mainPlaceId: String) {
		viewModelScope.launch {
			try {
//				val mainPlaces = getMainPlacesUseCase()
//				println("Loaded mainPlaces: $mainPlaces")
//				val selectedMainPlace = mainPlaces.find { it.id == mainPlaceId }
//				println("Selected MainPlace: $selectedMainPlace")
				val mainPlaces = getMainPlacesUseCase()
				val selectedMainPlace = mainPlaces.find { it.id.toString() == mainPlaceId }

				uiState = uiState.copy(
					places = selectedMainPlace?.allPlaces ?: emptyList(),
					isLoading = false
				)
			} catch (e: Exception) {
				uiState = uiState.copy(
					error = e.message ?: "Unexpected error",
					isLoading = false
				)
			}
		}
	}
}



data class PlacesUiState(
	val isLoading: Boolean = true,
	val places: List<AllPlaces2> = emptyList(),
	val error: String? = null
)
