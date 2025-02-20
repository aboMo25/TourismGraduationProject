package com.data.model.response
import com.data.model.DataProductModel
import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponse(
    val `data`: List<DataProductModel>,
    val msg: String
) {
    fun toProductList() = com.domain.model.ProductListModel(
        products = `data`.map { it.toProduct() },
        msg = msg
    )
}