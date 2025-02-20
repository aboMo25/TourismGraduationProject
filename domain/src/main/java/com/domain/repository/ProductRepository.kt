package com.domain.repository

import com.domain.model.Product
import com.domain.model.ProductListModel
import com.domain.network.ResultWrapper

interface ProductRepository {
    suspend fun getProducts(category:Int?) : ResultWrapper<ProductListModel>
}