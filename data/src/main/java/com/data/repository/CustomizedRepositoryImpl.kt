package com.data.repository

import com.data.model.local.TourismData
import com.domain.model.CustomizedTripData
import com.domain.repository.CustomizedRepository

class CustomizedRepositoryImpl: CustomizedRepository {
	override suspend fun getData(): List<CustomizedTripData> {
		return TourismData.customizedTripData
	}
}