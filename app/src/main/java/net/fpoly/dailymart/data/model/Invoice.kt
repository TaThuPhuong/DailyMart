package net.fpoly.dailymart.data.model

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.model.param.InvoiceParam
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Invoice(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("user") val user: User = User(),
    @SerializedName("customer") val customer: Customer = Customer(),
    @SerializedName("invoiceType") val type: String = "",
    @SerializedName("invoiceDetails") val invoiceDetails: ArrayList<DetailInvoice> = arrayListOf(),
    @SerializedName("numberId") val numberID: String = "",
    @SerializedName("dateCreated") val createAt: Long = System.currentTimeMillis(),
    @SerializedName("totalBill") val totalBill: Long = 0
) : Parcelable {
    fun typeToString(context: Context): String {
        return context.getString(InvoiceType.valueOf(type).value)
    }

    fun toParam(): InvoiceParam {
        return InvoiceParam(
            idUser = user.id,
            idCustomer = customer.id,
            products = invoiceDetails.detailInvoiceToParam(),
            invoiceType = type,
            totalBill = totalBill
        )
    }
}

fun ArrayList<DetailInvoice>.detailInvoiceToParam(): ArrayList<ProductInvoiceParam> {
    val result = this.map {
        ProductInvoiceParam(
            id = it.product.id,
            name = it.product.name,
            unitPrice = it.unitPrice,
            total = it.totalPrice,
            expiryDate = it.expiry,
            quantity = it.quantityProduct
        )
    }
    return ArrayList(result)
}

@Parcelize
data class DetailInvoice(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("product") val product: DetailProductInvoice = DetailProductInvoice(),
    @SerializedName("totalPrice") val totalPrice: Long = 0,
    @SerializedName("unitPrice") val unitPrice: Long = 0,
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("expiryDate") val expiry: Long = 0,
    @SerializedName("quantityProduct") val quantityProduct: Int = 0,
    ) : java.io.Serializable, Parcelable

@Parcelize
data class DetailProductInvoice(
    @SerializedName("_id") var id: String = "",
    @SerializedName("productName") var name: String = "",
    @SerializedName("barcode") var barcode: String = "",
    @SerializedName("supplier") var supplier: String = "",
    @SerializedName("industry") var category: String = "",
    @SerializedName("importPrice") var importPrice: Double = 0.0,
    @SerializedName("sellPrice") var sellPrice: Double = 0.0,
    @SerializedName("imageProduct") var imageProduct: String = "",
    @SerializedName("unit") var unit: String = "",
    @SerializedName("totalQuantity") var totalQuantity: String = ""
) : Parcelable


enum class InvoiceType(val value: Int) {
    IMPORT(R.string.invoice_import),
    EXPORT(R.string.invoice_sell),
    REFUND(R.string.invoice_sell)
}

enum class CustomerName(val value: Int) {
    FIT(R.string.name_customer)
}


