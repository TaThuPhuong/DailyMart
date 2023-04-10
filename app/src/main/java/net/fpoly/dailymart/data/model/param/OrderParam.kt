package net.fpoly.dailymart.data.model.param

data class OrderParam(
    val idUser: String,
    val idCustomer: String,
    val products: List<ProductByOrder>,
    val invoiceType: String,
    val dateCreate: Long,
    val totalBill: Long
)
