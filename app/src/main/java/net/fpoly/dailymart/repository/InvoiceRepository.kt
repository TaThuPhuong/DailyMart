package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.Response

interface InvoiceRepository {

    suspend fun getInvoices(token: String) : Response<ArrayList<Invoice>>

    suspend fun getInvoice(token: String, id: String) : Response<Invoice>

    suspend fun insertInvoice(token: String) : Response<Invoice>

    suspend fun deleteInvoice(token: String, id: String) : Response<Unit>

}