package net.fpoly.dailymart.data.model

import net.fpoly.dailymart.data.model.param.ProductByOrder

data class OrderResponse(
    val invoiceType: String,
    val dateCreated: String,
    val invoiceDetails: List<InvoiceDetailResponse>
)
