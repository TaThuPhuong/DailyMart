package net.fpoly.dailymart.data.model.param

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ForgotPass(
    @SerializedName("mail")
    val mail: String = "",
) : Serializable {
    fun checkValidate(): Boolean =
        mail.trim().isNotEmpty()
}

data class ResponseForgot(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: String = "",
) : Serializable