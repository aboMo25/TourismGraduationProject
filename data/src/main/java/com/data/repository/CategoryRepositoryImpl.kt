package com.data.repository

import com.data.model.CategoryDataModel
import com.domain.model.CategoriesListModel
import com.domain.model.Product
import com.domain.network.NetworkService
import com.domain.network.ResultWrapper
import com.domain.repository.CategoryRepository
import com.domain.repository.ProductRepository

class CategoryRepositoryImpl(private val networkService: NetworkService) : CategoryRepository {
    override suspend fun getCategories(): ResultWrapper<CategoriesListModel> {
        return networkService.getCategories()
    }
}