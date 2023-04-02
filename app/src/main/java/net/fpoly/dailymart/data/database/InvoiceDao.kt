package net.fpoly.dailymart.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.model.Invoice

@Dao
interface InvoiceDao {

    @Query("Select * from INVOICE Order by date_create DESC")
    suspend fun getInvoices(): List<Invoice>

    @Query("select * from INVOICE where id = :invoiceId")
    suspend fun getInvoice(invoiceId: String): Invoice

    @Insert
    suspend fun insertInvoice(invoice: Invoice)

    @Delete
    suspend fun deleteInvoice(invoice: Invoice)

    @Update
    suspend fun updateInvoice(invoice: Invoice)

    @Query(
        "SELECT expiry.expiry_date, products.name, invoice_detail.quantity, invoice.total\n" +
            "FROM expiry\n" +
            "INNER JOIN products ON expiry.product_id = products.id\n" +
            "INNER JOIN invoice_detail ON products.id = invoice_detail.product_id\n" +
            "INNER JOIN invoice ON invoice_detail.invoice_id = invoice.id\n" +
            "WHERE invoice.invoice_type = 'IMPORT'"
    )
    fun getOrder(): Flow<List<OrderInfo>>
}

data class OrderInfo(
    var name: String = "",
    var quantity: Int = 0,
    var expiry_date: String = "",
    var total: Long = 0
)
