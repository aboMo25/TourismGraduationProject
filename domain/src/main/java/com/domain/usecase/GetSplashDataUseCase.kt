package com.domain.usecase

import com.domain.model.SplashScreenData
import com.domain.repository.AllCategoriesRepository
import com.domain.repository.SplashDataRepository

class GetSplashDataUseCase(private val repository: SplashDataRepository) {
	suspend operator fun invoke(): List<SplashScreenData> {
		return repository.getSplashScreens()
	}
}