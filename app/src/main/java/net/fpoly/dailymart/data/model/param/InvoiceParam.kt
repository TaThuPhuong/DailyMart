package net.fpoly.dailymart.data.model.param

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import net.fpoly.dailymart.data.model.ProductInvoiceParam
import java.io.Serializable

@Parcelize
class InvoiceParam(
    @SerializedName("idUser") var idUser: String = "",
    @SerializedName("idCustomer") var idCustomer: String = "642d285acf62ee68ba804759",
    @SerializedName("products") var products: ArrayList<ProductInvoiceParam> = arrayListOf(),
    @SerializedName("invoiceType") var invoiceType: String = "",
    @SerializedName("totalBill") var totalBill: Long = 0,
) : Parcelable {
    fun isRefundEmpty() = this.products.any{it.quantity < 0}
}