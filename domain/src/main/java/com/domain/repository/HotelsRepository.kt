package com.domain.repository

import com.domain.model.Hotels
import com.domain.model.Hotels2

interface HotelsRepository {
//	suspend fun getHotels() : List<Hotels>
	suspend fun getHotels() : List<Hotels2>
}