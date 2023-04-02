package net.fpoly.dailymart.data.model

data class ProductResponse(
    val data: ProductResponseData,
)

data class ProductResponseData(
    val productName: String,
    val barcode: String,
)
