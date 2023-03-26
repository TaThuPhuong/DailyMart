package net.fpoly.dailymart.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import net.fpoly.dailymart.data.model.Invoice

@Dao
interface InvoiceDao {

    @Query("Select * from INVOICE Order by date_create DESC")
    suspend fun getInvoices() :List<Invoice>

    @Query("select * from INVOICE where id = :invoiceId")
    suspend fun getInvoice(invoiceId: String) : Invoice

    @Insert
    suspend fun insertInvoice(invoice: Invoice)

    @Delete
    suspend fun deleteInvoice(invoice: Invoice)

    @Update
    suspend fun updateInvoice(invoice: Invoice)

}