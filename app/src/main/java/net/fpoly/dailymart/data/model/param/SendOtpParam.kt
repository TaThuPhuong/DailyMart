package net.fpoly.dailymart.data.model.param

import com.google.gson.annotations.SerializedName

data class SendOtpParam(
    @SerializedName("id") val id: String = "",
    @SerializedName("token") val token: String = "",
    @SerializedName("newPassword") val newPassword: String = "",
) {
    fun checkValidate(): Boolean =
        !(token.trim().isEmpty() || newPassword.trim().isEmpty())
}