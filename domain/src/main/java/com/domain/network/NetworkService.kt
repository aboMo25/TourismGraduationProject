package com.domain.network

import com.domain.model.AddressDomainModel
import com.domain.model.CartItemModel
import com.domain.model.CartModel
import com.domain.model.CartSummary
import com.domain.model.CategoriesListModel
import com.domain.model.OrdersListModel
import com.domain.model.Product
import com.domain.model.ProductListModel
import com.domain.model.UserDomainModel
import com.domain.model.request.AddCartRequestModel

interface NetworkService {
    suspend fun getProducts(
        category: Int?
    ): ResultWrapper<ProductListModel>

    suspend fun getCategories(): ResultWrapper<CategoriesListModel>

    suspend fun addProductToCart(
        request: AddCartRequestModel,
        userId: Long
    ): ResultWrapper<CartModel>

    suspend fun getCart(
        userId: Long
    ): ResultWrapper<CartModel>

    suspend fun updateQuantity(
        cartItemModel: CartItemModel,
        userId: Long
    ): ResultWrapper<CartModel>

    suspend fun deleteItem(
        cartItemId: Int,
        userId: Long
    ): ResultWrapper<CartModel>

    suspend fun getCartSummary(
        userId: Long
    ): ResultWrapper<CartSummary>

    suspend fun placeOrder(
        address: AddressDomainModel,
        userId: Long
    ): ResultWrapper<Long>

    suspend fun getOrderList(
        userId: Long
    ): ResultWrapper<OrdersListModel>

    suspend fun login(
        email: String,
        password: String
    ): ResultWrapper<UserDomainModel>

    suspend fun register(
        email: String,
        password: String,
        name: String
    ): ResultWrapper<UserDomainModel>
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val exception: Exception) : ResultWrapper<Nothing>()
}