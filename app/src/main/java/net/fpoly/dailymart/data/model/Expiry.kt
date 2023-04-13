package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat

data class Expiry(
    @SerializedName("_id") val id: String = "",
    @SerializedName("product") var productId: String = "",
    @SerializedName("expiryDate") var expiryDate: String = "",
    @SerializedName("quantityExp") var quantity: Int = 0,
) : Serializable

data class ExpiryRes(
    @SerializedName("_id") val id: String = "",
    @SerializedName("product") var productId: String = "",
    @SerializedName("expiryDate") var expiryDate: Long = 0,
    @SerializedName("quantityExp") var quantity: Int = 0,
) : Serializable

data class ExpiryCheck(
    val id: String = "",
    var productId: String = "",
    var productName: String = "",
    var expiryDate: Long = 0,
    var quantity: Int = 0,
    var image: String = "",
)