package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.InvoiceRefund
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.param.InvoiceParam
import net.fpoly.dailymart.repository.InvoiceRepository

class InvoiceRepositoryImpl : InvoiceRepository {

    private val remoteDataInvoice = ServerInstance.apiInvoiceApi
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun getInvoices(token: String): Response<ArrayList<Invoice>> =
        withContext(ioDispatcher) {
            try {
                val res = remoteDataInvoice.getInvoices(token)
                if (res.isSuccess()) {
                    Response.Success(res.result, res.message)
                } else {
                    Response.Error(res.message)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Response.Error(ERROR_CONNECTED)
            }
        }

    override suspend fun getInvoice(token: String, id: String): Response<Invoice> =
        withContext(ioDispatcher) {
            try {
                val res = remoteDataInvoice.getInvoice(token, id)
                if (res.isSuccess()) {
                    Response.Success(res.result, res.message)
                } else {
                    Response.Error(res.message)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Response.Error(ERROR_CONNECTED)
            }
        }

    override suspend fun insertInvoice(
        token: String,
        invoiceParam: InvoiceParam
    ) = withContext(ioDispatcher) {
        try {
            val res = remoteDataInvoice.insertInvoice(token, invoiceParam)
            if (res.isSuccess()) {
                Response.Success(Unit)
            } else {
                Response.Error(res.message)
            }
        } catch (ex: Exception) {
            Response.Error(ERROR_CONNECTED)
        }
    }

    override suspend fun deleteInvoice(token: String, id: String): Response<Unit> = withContext(ioDispatcher) {
        try {
            val res = remoteDataInvoice.removeInvoice(token, id)
            if (res.isSuccess()) {
                Response.Success(Unit)
            } else {
                Response.Error(res.message)
            }
        } catch (ex: Exception) {
            Response.Error(ERROR_CONNECTED)
        }
    }

    override suspend fun updateInvoice(
        token: String,
        id: String,
        invoiceParam: InvoiceParam
    ) = withContext(Dispatchers.IO) {
        try {
            val res = remoteDataInvoice.updateInvoice(token, id, invoiceParam)
            if (res.isSuccess()) {
                Response.Success(Unit)
            } else {
                Response.Error(res.message)
            }
        } catch (ex: Exception) {
            Response.Error(ERROR_CONNECTED)
        }
    }

    override suspend fun refundInvoice(token: String, invoiceParam: InvoiceRefund): Response<Unit> =
        withContext(ioDispatcher) {
            try {
                val res = remoteDataInvoice.refundInvoice(token, invoiceParam)
                if (res.isSuccess()) {
                    Response.Success(Unit)
                } else {
                    Response.Error(res.message)
                }
            } catch (ex: Exception) {
                Response.Error(ERROR_CONNECTED)
            }
        }

    companion object {
        const val TAG = "BT"
        const val ERROR_CONNECTED = "Kết nối thất bại"
    }
}