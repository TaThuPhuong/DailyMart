package net.fpoly.dailymart.data.model

data class InvoiceDetailResponse(
    val product: String,
    val quantity: Int,
    val price: Long,
)
