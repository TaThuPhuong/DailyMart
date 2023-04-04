package net.fpoly.dailymart.data.model

data class InvoiceDetailResponse(
    val product: ProductResponseData,
    val quantity: Int,
    val price: Long,
)
