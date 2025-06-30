package com.domain.model

import com.domain.model.response.CartItemResponse

data class UserDomainModel(
    val id: Int?,
    val username: String,
    val email: String,
    val name: String,
    val cart: List<CartItemResponse>
)
