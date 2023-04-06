package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Supplier(
    @SerializedName("_id") var id: String = UUID.randomUUID().toString(),
    @SerializedName("supplierName") val supplierName: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("createdAt") var createdAt: String = "",
)

data class SupplierParam(
    @SerializedName("supplierName") val supplierName: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    )

data class ResultData<T>(
    var status: Int = 0,
    var message: String = "",
    @SerializedName("data") var result: T
) {
    fun isSuccess(): Boolean {
        return status == 1
    }
}
