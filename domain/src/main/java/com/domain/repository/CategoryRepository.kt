package com.domain.repository

import com.domain.model.CategoriesListModel
import com.domain.model.Product
import com.domain.network.ResultWrapper

interface CategoryRepository {
    suspend fun getCategories(): ResultWrapper<CategoriesListModel>
}