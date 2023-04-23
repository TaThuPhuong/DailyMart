package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class Supplier(
    @SerializedName("_id") var id: String = UUID.randomUUID().toString(),
    @SerializedName("supplierName") val supplierName: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("createdAt") var createdAt: String = "",
    @SerializedName("representativeName") var representativeName: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("status") var status: Boolean = true,
) : Serializable

data class SupplierParam(
    @SerializedName("supplierName") val supplierName: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("representativeName") var representativeName: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("status") var status: Boolean = true,
)

data class SupplierParamAdd(
    @SerializedName("supplierName") val supplierName: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("representativeName") var representativeName: String = "",
    @SerializedName("address") var address: String = ""
)

data class ResultData<T>(
    var status: Int = 0,
    var message: String = "",
    var currentPage: Int = 0,
    var totalPage: Int = 0,
    @SerializedName("data") var result: T
) {
    fun isSuccess(): Boolean {
        return status == 1
    }
}
