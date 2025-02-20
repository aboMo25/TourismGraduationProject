package com.data.repository

import com.domain.model.Product
import com.domain.model.ProductListModel
import com.domain.network.NetworkService
import com.domain.network.ResultWrapper
import com.domain.repository.ProductRepository

class ProductRepositoryImpl(private val networkService: NetworkService) : ProductRepository {
    override suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel> {
        return networkService.getProducts(category)
    }
}