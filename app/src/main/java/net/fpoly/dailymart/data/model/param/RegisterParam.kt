package net.fpoly.dailymart.data.model.param

import com.google.gson.annotations.SerializedName
import net.fpoly.dailymart.utils.ROLE

data class RegisterParam(
   @SerializedName("name") val name: String = "",
   @SerializedName("password") val password: String = "",
   @SerializedName("email")  val email: String = "",
   @SerializedName("phoneNumber") val phoneNumber: String = "",
   @SerializedName("status")  val status: Boolean = true,
   @SerializedName("role")  val role: ROLE = ROLE.staff,
   @SerializedName("deviceId") val deviceId: String = "",
   @SerializedName("linkAvt") val linkAvt: String = "",
)