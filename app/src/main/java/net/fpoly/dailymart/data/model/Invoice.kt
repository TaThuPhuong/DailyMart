package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

//foreignKeys = [
//ForeignKey(
//entity = User::class,
//parentColumns = arrayOf("id"),
//childColumns = arrayOf("user_id")
//)
//]
@Entity(tableName = "invoice")
data class Invoice(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "user_id") var userId: String = "",
    @ColumnInfo(name = "invoice_type") var invoiceType: String = InvoiceType.SELL.name,
    @ColumnInfo(name = "date_create") val dateCreate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "total") var total: Double = 0.0,
)

enum class InvoiceType {
    IMPORT, SELL, DEDUCTION
}
