package com.domain.usecase

import com.domain.repository.SplashDataRepository

class HandleSplashNavigationUseCase(
	private val repository: SplashDataRepository
) {
	suspend operator fun invoke(currentIndex: Int, totalScreens: Int): Boolean {
		val nextIndex = currentIndex + 1
		return if (nextIndex < totalScreens) {
			repository.saveCurrentIndex(nextIndex)
			false
		} else {
			true
		}
	}
}
