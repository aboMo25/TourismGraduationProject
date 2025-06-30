package com.domain.usecase

import com.domain.model.Hotels
import com.domain.model.Hotels2
import com.domain.repository.HotelsRepository

class GetAllHotelsUseCase(private val repository : HotelsRepository) {
	suspend operator fun invoke(): List<Hotels2> {
		return repository.getHotels()
	}

}