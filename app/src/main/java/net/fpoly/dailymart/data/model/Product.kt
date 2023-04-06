package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

data class Product(
    @PrimaryKey @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "supplier_id") var supplierId: String = "",
    @ColumnInfo(name = "category_id") var categoryId: String = "",
    @ColumnInfo(name = "barcode") var barcode: String = "",
    @ColumnInfo(name = "unit") var unit: String = "",
    @ColumnInfo(name = "img_product") var img_product: String? = "",
) {
    companion object {
        const val TABLE_NAME = "products"
    }
}
