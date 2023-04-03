package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.database.InvoiceDetailDao
import net.fpoly.dailymart.data.model.InvoiceDetail
import net.fpoly.dailymart.repository.InvoiceDetailRepository

class InvoiceDetailRepositoryImpl(
    private val invoiceDDao: InvoiceDetailDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : InvoiceDetailRepository {

    override suspend fun getInvoicesD(): Result<List<InvoiceDetail>> = withContext(ioDispatcher) {
        Result.success(invoiceDDao.getInvoicesD())
    }

    override suspend fun getInvoiceD(invoiceId: String): Result<InvoiceDetail> =
        withContext(ioDispatcher) {
            Result.success(invoiceDDao.getInvoiceD(invoiceId))
        }

    override suspend fun insertInvoiceD(invoice: InvoiceDetail) = withContext(ioDispatcher) {
        invoiceDDao.insertInvoiceD(invoice)
    }

    override suspend fun deleteInvoiceD(invoice: InvoiceDetail) = withContext(ioDispatcher) {
        invoiceDDao.deleteInvoiceD(invoice)
    }

    override suspend fun updateInvoiceD(invoice: InvoiceDetail) = withContext(ioDispatcher) {
        invoiceDDao.updateInvoiceD(invoice)
    }
}
