package com.domain.usecase

import com.domain.model.CustomizedTripData
import com.domain.repository.CustomizedRepository

class GetCustomizedTripUseCase(private val repository: CustomizedRepository) {
	suspend operator fun invoke () : List<CustomizedTripData>{
		return repository.getData()
	}
}