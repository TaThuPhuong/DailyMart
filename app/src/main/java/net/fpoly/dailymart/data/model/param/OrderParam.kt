package net.fpoly.dailymart.data.model.param

data class OrderParam(
    val idUser: String,
    val products: List<ProductByOrder>,
    val invoiceType: String,
    val dateCreate: String,
)
