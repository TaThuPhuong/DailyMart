package net.fpoly.dailymart.repository

import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.database.OrderInfo
import net.fpoly.dailymart.data.model.Invoice

interface InvoiceRepository {

    suspend fun getInvoices() : Result<List<Invoice>>

    suspend fun getInvoice(invoiceId: String ) : Result<Invoice>

    suspend fun insertInvoice(invoice: Invoice)

    suspend fun deleteInvoice(invoice: Invoice)

    suspend fun updateInvoice(invoice: Invoice)

//    fun getOrders() : Flow<List<OrderInfo>>
}