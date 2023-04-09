package net.fpoly.dailymart.data.model

data class InvoiceDetailResponse(
    val product: ProductResponseData,
    val quantityProduct: Double,
    val totalPrice: Double,
)
