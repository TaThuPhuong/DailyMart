package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.param.InvoiceParam

interface InvoiceRepository {

    suspend fun getInvoices(token: String) : Response<ArrayList<Invoice>>

    suspend fun getInvoice(token: String, id: String) : Response<Invoice>

    suspend fun insertInvoice(token: String, invoiceParam: InvoiceParam) : Response<Unit>

    suspend fun deleteInvoice(token: String, id: String) : Response<Unit>

    suspend fun updateInvoice(token: String, id: String, invoiceParam: InvoiceParam)  : Response<Unit>

}