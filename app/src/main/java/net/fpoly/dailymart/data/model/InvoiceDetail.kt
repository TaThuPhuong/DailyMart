package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "invoice_detail")
data class InvoiceDetail(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "invoice_id") val invoiceId: Int = 0,
    @ColumnInfo(name = "product_id") val productId: Int = 0,
    @ColumnInfo(name = "quantity") var quantity: Int = 0,
    @ColumnInfo(name = "into_money") var intoMoney: Double = 0.0,
)