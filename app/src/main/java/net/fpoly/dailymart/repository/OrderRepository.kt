package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.ListOrderResponse
import net.fpoly.dailymart.data.model.param.OrderParam
import net.fpoly.dailymart.data.model.OrderResponse
import retrofit2.Call

class OrderRepository {

    private val invoiceService = ServerInstance.apiInvoiceApi

    suspend fun insertInvoice(invoice: OrderParam, token: String): Call<OrderResponse> {
        return invoiceService.insertInvoice(token, invoice)
    }

    suspend fun getInvoices(token: String): Call<ListOrderResponse> {
        return invoiceService.getAllInvoice(token)
    }
}
