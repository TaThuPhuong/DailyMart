package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Customer(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("customerName") val name: String = "",
    @SerializedName("customerPhone") val phoneNumber: String = "",
)