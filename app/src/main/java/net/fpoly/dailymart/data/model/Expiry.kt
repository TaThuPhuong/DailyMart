package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

data class Expiry(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "product_id") val productId: String = "",
    @ColumnInfo(name = "expiry_date") val expiryDate: Long = 0,
    @ColumnInfo(name = "quantity") var quantity: Int = 0,
)
