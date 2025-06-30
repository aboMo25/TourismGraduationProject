package com.domain.repository

import com.domain.model.SplashScreenData


interface SplashDataRepository {
	suspend fun getSplashScreens(): List<SplashScreenData>
	suspend fun getCurrentIndex(): Int
	suspend fun saveCurrentIndex(index: Int)
}