package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("_id") var id: String = "",
    @SerializedName("productName") var name: String = "",
    @SerializedName("supplier") var supplier: Supplier = Supplier(),
    @SerializedName("industry") var category: Category = Category(),
    @SerializedName("barcode") var barcode: String = "",
    @SerializedName("unit") var unit: String = "",
    @SerializedName("img_product") var img_product: String? = "",
) {
    companion object {
        const val TABLE_NAME = "products"
    }
}

data class ProductParam(
    @SerializedName("name") var name: String = "",
    @SerializedName("barcode") var barcode: String = "",
    @SerializedName("supplier") var supplier: String = "",
    @SerializedName("industry") var category: String = "",
    @SerializedName("importPrice") var importPrice: Double = 0.0,
    @SerializedName("sellPrice") var sellPrice: Double = 0.0,
    @SerializedName("imageProduct") var imageProduct: String = "",
    @SerializedName("unit") var unit: String = ""
)
