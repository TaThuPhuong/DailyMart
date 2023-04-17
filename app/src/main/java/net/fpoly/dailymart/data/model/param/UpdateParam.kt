package net.fpoly.dailymart.data.model.param

import com.google.gson.annotations.SerializedName
import net.fpoly.dailymart.utils.ROLE

data class UpdateParam(
    @SerializedName("_id") var _id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("status") var status: Boolean = true,
    @SerializedName("role") var role: ROLE = ROLE.staff,
    @SerializedName("deviceId") var deviceId: String = "",
    @SerializedName("linkAvt") var linkAvt: String = "",
) {
    constructor(user: Datum) : this() {
        this._id = user._id
        this.name = user.name
        this.email = user.email
        this.phoneNumber = user.phoneNumber
        this.status = user.status
        this.role = user.role
        this.deviceId = user.deviceId
        this.linkAvt = user.linkAvt
    }

    fun checkValidate(): Boolean =
        !(name.trim().isEmpty() || email.trim().isEmpty() || phoneNumber.trim().isEmpty())

    sealed class ResponseUser<out R> {
        data class Success<T>(val data: T) : ResponseUser<T>()
        data class Error(val message: String) : ResponseUser<Nothing>()
    }

    val ResponseUser<*>.succeeded
        get() = this is ResponseUser.Success && data != null
}

