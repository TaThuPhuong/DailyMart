package net.fpoly.dailymart.data.database

import androidx.room.*
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.InvoiceDetail

@Dao
interface InvoiceDetailDao {

    @Query("Select * from INVOICE_DETAIL")
    suspend fun getInvoicesD(): List<InvoiceDetail>

    @Query("select * from INVOICE_DETAIL where id = :invoiceId")
    suspend fun getInvoiceD(invoiceId: String): InvoiceDetail

    @Insert
    suspend fun insertInvoiceD(invoice: InvoiceDetail)

    @Delete
    suspend fun deleteInvoiceD(invoice: InvoiceDetail)

    @Update
    suspend fun updateInvoiceD(invoice: InvoiceDetail)
}
