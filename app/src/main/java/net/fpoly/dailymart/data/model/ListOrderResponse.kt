package net.fpoly.dailymart.data.model

data class ListOrderResponse(
    val status: String,
    val message: String,
    val data: ArrayList<OrderResponse>,
)
