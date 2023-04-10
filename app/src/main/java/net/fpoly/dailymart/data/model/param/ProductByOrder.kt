package net.fpoly.dailymart.data.model.param

data class ProductByOrder(
    val idProduct: String,
    val unitPrice: Long,
    val quantityPro: Int,
    val totalPrice: Long,
    val expiryDate: Long
)
