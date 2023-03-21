package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_price", foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("product_id")
        )
    ]
)
data class ProductPrice(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "product_id") val productId: Int = 0,
    @ColumnInfo(name = "import_price") var importPrice: Double = 0.0,
    @ColumnInfo(name = "sell_price") var sellPrice: Double = 0.0,
    @ColumnInfo(name = "effective_date") val effectiveDate: Long = 0,
)