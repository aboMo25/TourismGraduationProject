package com.data.repository

import com.data.model.local.TourismData
import com.domain.model.SplashScreenData
import com.domain.repository.SplashDataRepository

class SplashDataRepositoryImpl :SplashDataRepository {
	private var currentIndex = 0
	override suspend fun getSplashScreens(): List<SplashScreenData> {
		// Convert from data model to domain model
		return TourismData.splashScreens
//		return TourismData.splashScreens.map {
//			SplashScreenData(it.image, it.title, it.description)
//		}
	}

	override suspend fun getCurrentIndex(): Int = currentIndex

	override suspend fun saveCurrentIndex(index: Int) {
		currentIndex = index
	}
}