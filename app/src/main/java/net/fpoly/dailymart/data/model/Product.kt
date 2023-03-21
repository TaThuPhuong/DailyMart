package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "products", foreignKeys = [
        ForeignKey(
            entity = Supplier::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("supplier_id")
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id")
        )
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "supplier_id") var supplierId: Int = 0,
    @ColumnInfo(name = "category_id") var categoryId: Int = 0,
    @ColumnInfo(name = "barcode") var barcode: String = "",
    @ColumnInfo(name = "unit") var unit: String = "",
    @ColumnInfo(name = "img_product") var img_product: String = "",
)