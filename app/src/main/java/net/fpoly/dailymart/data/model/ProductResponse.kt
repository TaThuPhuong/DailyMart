package net.fpoly.dailymart.data.model

data class ProductResponse(
    val data: ProductResponseData,
    val status: Int,
    val message: String
)

data class ProductResponseData(
    val productName: String,
    val barcode: String,
)

data class ListProductResponse(
    val data: List<ProductResponseData>,
    val status: Int,
    val message: String
)