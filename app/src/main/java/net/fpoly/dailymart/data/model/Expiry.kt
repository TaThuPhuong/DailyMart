package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expiry", foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("product_id")
    )]
)
data class Expiry(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "expiry_date") val expiryDate: Long,
    @ColumnInfo(name = "quantity") var quantity: Int = 0,
)
