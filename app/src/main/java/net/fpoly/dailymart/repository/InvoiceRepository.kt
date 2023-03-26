package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.Invoice

interface InvoiceRepository {

    suspend fun getInvoices() : Result<List<Invoice>>

    suspend fun getInvoice(invoiceId: String ) : Result<Invoice>

    suspend fun insertInvoice(invoice: Invoice)

    suspend fun deleteInvoice(invoice: Invoice)

    suspend fun updateInvoice(invoice: Invoice)
}