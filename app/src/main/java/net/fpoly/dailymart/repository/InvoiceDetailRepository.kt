package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.InvoiceDetail

interface InvoiceDetailRepository {
    suspend fun getInvoicesD(): Result<List<InvoiceDetail>>

    suspend fun getInvoiceD(invoiceId: String): Result<InvoiceDetail>

    suspend fun insertInvoiceD(invoice: InvoiceDetail)

    suspend fun deleteInvoiceD(invoice: InvoiceDetail)

    suspend fun updateInvoiceD(invoice: InvoiceDetail)
}
