package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "invoice")
data class Invoice(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "user_id") val userId: String = "",
    @ColumnInfo(name = "invoice_type") var invoiceType: InvoiceType = InvoiceType.SELL,
    @ColumnInfo(name = "date_create") val dateCreate: Long = 0,
    @ColumnInfo(name = "total") var total: Double = 0.0,
)

enum class InvoiceType {
    IMPORT, SELL
}
