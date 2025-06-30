package com.data.model.response

import com.domain.model.response.CartItemResponse
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int?,
    val username: String,
    val email: String,
    val name: String,
    val cart: List<CartItemResponse> // <-- CHANGE THIS to the DTO type
) {
    fun toDomainModel() = com.domain.model.UserDomainModel(
        id = id,
        username = username,
        email = email,
        name = name,
        cart = cart.map { it } // <-- Map each CartItemResponse to CartItem2
    )
}