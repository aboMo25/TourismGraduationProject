package com.data.repository

import com.data.model.local.TourismData
import com.domain.repository.AllCategoriesRepository

class AllCategoriesRepositoryImpl :AllCategoriesRepository {
	override suspend fun getAllCategories(): List<String> {
		return TourismData.categories
	}
}