package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @SerializedName("_id") var id: String = "",
    @SerializedName("productName") var name: String = "",
    @SerializedName("supplier") var supplier: Supplier = Supplier(),
    @SerializedName("industry") var category: Category = Category(),
    @SerializedName("barcode") var barcode: String = "",
    @SerializedName("importPrice") var importPrice: Int = 0,
    @SerializedName("sellPrice") var sellPrice: Int = 0,
    @SerializedName("totalQuantity") var totalQuantity: Int = 0,
    @SerializedName("unit") var unit: String = "",
    @SerializedName("img_product") var img_product: String = "",
    @SerializedName("expires") var expires: ArrayList<ExpiryRes> = ArrayList(),
    @SerializedName("createdAt") var createdAt: String = "",
) : Serializable {
    companion object {
        const val TABLE_NAME = "products"
    }
}

data class ProductParam(
    @SerializedName("name") var name: String = "",
    @SerializedName("barcode") var barcode: String = "",
    @SerializedName("supplier") var supplier: String = "",
    @SerializedName("industry") var category: String = "",
    @SerializedName("importPrice") var importPrice: Int = 0,
    @SerializedName("sellPrice") var sellPrice: Int = 0,
    @SerializedName("imageProduct") var imageProduct: String = "",
    @SerializedName("unit") var unit: String = "",
) {
    constructor(product: Product) : this() {
        this.name = product.name
        this.barcode = product.barcode
        this.category = product.category.id
        this.supplier = product.supplier.id
        this.imageProduct = product.img_product
        this.importPrice = product.importPrice
        this.sellPrice = product.sellPrice
        this.unit = product.unit
    }
}

data class ProductParamUpdate(
    @SerializedName("productName") var name: String = "",
    @SerializedName("barcode") var barcode: String = "",
    @SerializedName("supplier") var supplier: String = "",
    @SerializedName("industry") var category: String = "",
    @SerializedName("importPrice") var importPrice: Int = 0,
    @SerializedName("sellPrice") var sellPrice: Int = 0,
    @SerializedName("img_product") var imageProduct: String = "",
    @SerializedName("unit") var unit: String = "",
) {
    constructor(product: Product) : this() {
        this.name = product.name
        this.barcode = product.barcode
        this.category = product.category.id
        this.supplier = product.supplier.id
        this.imageProduct = product.img_product
        this.importPrice = product.importPrice
        this.sellPrice = product.sellPrice
        this.unit = product.unit
    }
}

data class ProductInvoiceParam(
    @SerializedName("idProduct") val idProduct: String = "",
    @SerializedName("unitPrice") val unitPrice: String = "",
    @SerializedName("quantityPro") val quantity: Int = 0,
    @SerializedName("totalPrice") val total : Int = 0,
    @SerializedName("expiryDate") val expiryDate: Long = 0,
)


fun ProductParam.checkValidate(): Boolean {
    return name.trim().isNotEmpty() && barcode.trim().isNotEmpty() && supplier.trim()
        .isNotEmpty() && category.trim().isNotEmpty() && importPrice != 0 && sellPrice != 0
}

fun ProductParamUpdate.checkValidate(): Boolean {
    return name.trim().isNotEmpty() && barcode.trim().isNotEmpty() && supplier.trim()
        .isNotEmpty() && category.trim().isNotEmpty() && importPrice != 0 && sellPrice != 0
}