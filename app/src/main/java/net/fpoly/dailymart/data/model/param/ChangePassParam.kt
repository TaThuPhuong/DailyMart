package net.fpoly.dailymart.data.model.param

import com.google.gson.annotations.SerializedName

data class ChangePassParam(
    @SerializedName("id") val id: String = "",
    @SerializedName("phoneNumber") val phoneNumber: String = "",
    @SerializedName("oldPass") val oldPass: String = "",
    @SerializedName("newPass") val newPass: String = "",
//    @SerializedName("confirmPass") val confirmPass: String? = "",
) {
    fun checkValidate(): Boolean =
        !(oldPass.trim().isEmpty() || newPass.trim().isEmpty() || phoneNumber.trim()
            .isEmpty())
}