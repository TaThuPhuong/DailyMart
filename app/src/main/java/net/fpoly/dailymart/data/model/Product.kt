package net.fpoly.dailymart.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import net.fpoly.dailymart.utils.Constant
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
    @SerializedName("productName") var name: String = "",
    @SerializedName("barcode") var barcode: String = "",
    @SerializedName("supplier") var supplier: String = "",
    @SerializedName("industry") var category: String = "",
    @SerializedName("importPrice") var importPrice: Int = 0,
    @SerializedName("sellPrice") var sellPrice: Int = 0,
    @SerializedName("img_product") var imageProduct: String = Constant.IMAGE_DEFAULT,
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
    @SerializedName("idProduct") val id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("unitPrice") var unitPrice: Int = 0,
    @SerializedName("quantityPro") var quantity: Int = 0,
    @SerializedName("totalPrice") var total: Int = 0,
    @SerializedName("expiryDate") var expiryDate: Long = 0,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeInt(unitPrice)
        parcel.writeInt(quantity)
        parcel.writeInt(total)
        parcel.writeLong(expiryDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductInvoiceParam> {
        override fun createFromParcel(parcel: Parcel): ProductInvoiceParam {
            return ProductInvoiceParam(parcel)
        }

        override fun newArray(size: Int): Array<ProductInvoiceParam?> {
            return arrayOfNulls(size)
        }
    }
}

@Parcelize
data class InvoiceRefund(
    @SerializedName("idUser") var idUser: String = "",
    @SerializedName("idInvoice") val id: String = "",
    @SerializedName("products") var products: ArrayList<ProductInvoiceParam> = arrayListOf(),
    @SerializedName("invoiceType") var invoiceType: String = "REFUND",
    @SerializedName("totalBill") var total: Long = 0,
) : Parcelable


fun ProductParam.checkValidate(): Boolean {
    return name.trim().isNotEmpty() && barcode.trim().isNotEmpty() && supplier.trim()
        .isNotEmpty() && category.trim()
        .isNotEmpty() && importPrice != 0 && sellPrice != 0 && unit.trim().isNotEmpty()
}

fun ProductParamUpdate.checkValidate(): Boolean {
    return name.trim().isNotEmpty() && barcode.trim().isNotEmpty() && supplier.trim()
        .isNotEmpty() && category.trim()
        .isNotEmpty() && importPrice != 0 && sellPrice != 0 && unit.trim().isNotEmpty()
}