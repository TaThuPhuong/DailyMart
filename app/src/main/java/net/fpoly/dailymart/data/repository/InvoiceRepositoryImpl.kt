package net.fpoly.dailymart.data.repository

import androidx.room.Query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.database.InvoiceDao
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.repository.InvoiceRepository

class InvoiceRepositoryImpl(
    private val invoiceDao: InvoiceDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : InvoiceRepository {

    override suspend fun getInvoices(): Result<List<Invoice>> = withContext(ioDispatcher) {
        Result.success(invoiceDao.getInvoices())
    }

    override suspend fun getInvoice(invoiceId: String): Result<Invoice> =
        withContext(ioDispatcher) {
            Result.success(invoiceDao.getInvoice(invoiceId))
        }

    override suspend fun insertInvoice(invoice: Invoice) = withContext(ioDispatcher) {
        invoiceDao.insertInvoice(invoice)
    }

    override suspend fun deleteInvoice(invoice: Invoice) = withContext(ioDispatcher) {
        invoiceDao.deleteInvoice(invoice)
    }

    override suspend fun updateInvoice(invoice: Invoice) = withContext(ioDispatcher) {
        invoiceDao.updateInvoice(invoice)
    }

}