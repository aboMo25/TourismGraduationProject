package com.data.model

import com.domain.model.CartSummary
import kotlinx.serialization.Serializable

@Serializable
data class CartSummaryResponse(
    val `data`: Summary,
    val msg: String
) {
    fun toCartSummary() = CartSummary(
        data = `data`.toSummaryData(),
        msg = msg
    )
}