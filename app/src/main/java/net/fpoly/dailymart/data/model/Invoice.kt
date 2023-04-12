package net.fpoly.dailymart.data.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import net.fpoly.dailymart.R
import java.util.*
import kotlin.collections.ArrayList

data class Invoice(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("user") val user: User = User(),
    @SerializedName("customer") val customer: Customer = Customer(),
    @SerializedName("invoiceType") val type: String = "",
    @SerializedName("invoiceDetails") val invoiceDetails: ArrayList<DetailInvoice> = arrayListOf(),
    @SerializedName("dateCreated") val createAt: Long = System.currentTimeMillis(),
    @SerializedName("totalBill") val totalBill: Long = 0
) {
    fun typeToString(context: Context): String {
        return context.getString(InvoiceType.valueOf(type).value)
    }
}

data class DetailInvoice(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("product") val product: DetailProductInvoice = DetailProductInvoice(),
    @SerializedName("quantityProduct") val quantityProduct: Int = 0,
    @SerializedName("totalPrice") val totalPrice: Int = 0,
    @SerializedName("unitPrice") val unitPrice: Int = 0,
    @SerializedName("createdAt") val createdAt: String = "",
)

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
)


enum class InvoiceType(val value: Int) {
    IMPORT(R.string.invoice_import),
    EXPORT(R.string.invoice_sell),
    REFUND(R.string.invoice_deduction)
}

enum class CustomerName(val value: Int) {
    FIT(R.string.name_customer)
}


