package com.domain.usecase

import com.domain.model.Hotels
import com.domain.repository.AllCategoriesRepository

class GetAllCategoriesUseCase (private val repository: AllCategoriesRepository) {
	suspend operator fun invoke(): List<String> {
		return repository.getAllCategories()
	}
}