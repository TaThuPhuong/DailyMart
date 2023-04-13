package net.fpoly.dailymart.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Customer(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("customerName") val name: String = "",
    @SerializedName("customerPhone") val phoneNumber: String = "",
) : java.io.Serializable, Parcelable

data class CustomerParam(
    @SerializedName("customerName") val name: String = "",
    @SerializedName("customerPhone") val phoneNumber: String = "",

)