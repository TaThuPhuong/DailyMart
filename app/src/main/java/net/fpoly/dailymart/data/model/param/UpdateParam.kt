package net.fpoly.dailymart.data.model.param

import com.google.gson.annotations.SerializedName
import net.fpoly.dailymart.utils.ROLE

data class UpdateParam(
    @SerializedName("_id") val _id: String? = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("password") val password: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("phoneNumber") val phoneNumber: String = "",
    @SerializedName("status") val status: Boolean = true,
    @SerializedName("role") val role: ROLE = ROLE.staff,
    @SerializedName("deviceId") val deviceId: String = "",
    @SerializedName("linkAvt") var linkAvt: String = "",
) {
    fun checkValidate(): Boolean =
        !(name.trim().isEmpty() || email.trim().isEmpty() || phoneNumber.trim()
            .isEmpty() || role.value.trim().isEmpty())

    sealed class ResponseUser<out R> {
        data class Success<T>(val data: T) : ResponseUser<T>()
        data class Error(val message: String) : ResponseUser<Nothing>()
    }

    val ResponseUser<*>.succeeded
        get() = this is ResponseUser.Success && data != null
}

