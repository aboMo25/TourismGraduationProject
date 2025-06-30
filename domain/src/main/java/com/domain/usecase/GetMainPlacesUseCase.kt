package com.domain.usecase

import com.domain.model.MainPlace2
import com.domain.repository.MainPlacesRepository

class GetMainPlacesUseCase( private val repository: MainPlacesRepository) {
	suspend operator fun invoke (): List<MainPlace2> {
		return repository.getMainPlaces()
	}
}
