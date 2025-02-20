package com.ui.feature.splash

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SplashViewModel : ViewModel() {
	private val _currentSplashData = MutableStateFlow<SplashScreenData>(TourismData.splashScreens.first())
	val currentSplashData: StateFlow<SplashScreenData> = _currentSplashData.asStateFlow()

	private val _shouldNavigateToLogin = MutableStateFlow(false)
	val shouldNavigateToLogin: StateFlow<Boolean> = _shouldNavigateToLogin.asStateFlow()

	private val _currentIndex = MutableStateFlow(0)
	val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()
	fun onNextClicked() {
		if (_currentIndex.value < TourismData.splashScreens.lastIndex) {
			_currentIndex.value++
			_currentSplashData.value = TourismData.splashScreens[_currentIndex.value]
		} else {
			_shouldNavigateToLogin.value = true
		}
	}

	fun resetNavigation() {
		_shouldNavigateToLogin.value = false
	}
}