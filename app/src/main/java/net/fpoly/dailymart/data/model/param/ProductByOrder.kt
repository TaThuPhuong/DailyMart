package net.fpoly.dailymart.data.model.param

data class ProductByOrder(
    val idProduct: String,
    val importPrice: Long,
    val quantity: Int,
    val expiryDate: String
)
