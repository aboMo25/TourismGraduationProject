package com.data.repository

import com.data.model.local.TourismData
import com.domain.model.Hotels
import com.domain.model.Hotels2
import com.domain.repository.HotelsRepository

class AllHotelsRepositoryImpl:HotelsRepository {
//	override suspend fun getHotels(): List<Hotels> {
//		return TourismData.hotels
//	}
	override suspend fun getHotels(): List<Hotels2> {
		return TourismData.hotels2
	}
}