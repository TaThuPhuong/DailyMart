package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class InvoiceDetail(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("product") val product: Product = Product(),
    @SerializedName("quantityProduct") val quantityProduct: Int = 0,
    @SerializedName("totalPrice") val totalPrice: Int = 0,
    @SerializedName("unitPrice") val unitPrice: Int = 0,
    @SerializedName("createdAt") val createdAt: String = "",
)