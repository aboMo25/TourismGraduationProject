package com.ui.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.SplashScreenData
import com.domain.usecase.GetSplashDataUseCase
import com.domain.usecase.HandleSplashNavigationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ui/feature/splash/SplashViewModel.kt
class SplashViewModel(
	private val getSplashScreensUseCase: GetSplashDataUseCase,
	private val handleNavigationUseCase: HandleSplashNavigationUseCase
) : ViewModel() {

	private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
	val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

	init {
		loadSplashData()
	}

	private fun loadSplashData() {
		viewModelScope.launch {
			_uiState.value = SplashUiState.Loading
			try {
				val screens = getSplashScreensUseCase()
				if (screens.isNotEmpty()) {
					_uiState.value = SplashUiState.Success(
						currentScreen = screens.first(),
						currentIndex = 0,
						totalScreens = screens.size
					)
				} else {
					_uiState.value = SplashUiState.Error("No splash screens available")
				}
			} catch (e: Exception) {
				_uiState.value = SplashUiState.Error(e.message ?: "Error loading splash screens")
			}
		}
	}

	fun onNextClicked() {
		val currentState = _uiState.value
		if (currentState is SplashUiState.Success) {
			viewModelScope.launch {
				val shouldNavigate = handleNavigationUseCase(
					currentState.currentIndex,
					currentState.totalScreens
				)

				if (shouldNavigate) {
					_uiState.value = currentState.copy(shouldNavigate = true)
				} else {
					val screens = getSplashScreensUseCase()
					_uiState.value = currentState.copy(
						currentScreen = screens[currentState.currentIndex + 1],
						currentIndex = currentState.currentIndex + 1
					)
				}
			}
		}
	}

	fun resetNavigation() {
		val currentState = _uiState.value
		if (currentState is SplashUiState.Success) {
			_uiState.value = currentState.copy(shouldNavigate = false)
		}
	}
}

sealed class SplashUiState {
	object Loading : SplashUiState()
	data class Success(
		val currentScreen: SplashScreenData,
		val currentIndex: Int,
		val totalScreens: Int,
		val shouldNavigate: Boolean = false
	) : SplashUiState()
	data class Error(val message: String) : SplashUiState()
}