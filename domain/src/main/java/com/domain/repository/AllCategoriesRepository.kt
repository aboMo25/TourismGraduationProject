package com.domain.repository

interface AllCategoriesRepository {
	suspend fun getAllCategories() : List<String>
}