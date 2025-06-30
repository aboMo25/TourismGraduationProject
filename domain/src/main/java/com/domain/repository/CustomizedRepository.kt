package com.domain.repository

import com.domain.model.CustomizedTripData

interface CustomizedRepository {
	suspend fun getData() : List<CustomizedTripData>
}