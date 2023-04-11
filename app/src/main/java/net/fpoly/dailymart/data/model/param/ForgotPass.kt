package net.fpoly.dailymart.data.model.param

import com.google.gson.annotations.SerializedName

data class ForgotPass(
    @SerializedName("mail")
    val mail: String = "",
) {
    fun checkValidate(): Boolean =
        mail.trim().isNotEmpty()
}