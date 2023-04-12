package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import java.io.Serializable

data class User(
    @SerializedName("_id") val id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("linkAvt") var avatar: String = Constant.AVATAR_DEFAULT,
    @SerializedName("email") var email: String = "",
    @SerializedName("phoneNumber") var phone: String = "",
    @SerializedName("role") var role: ROLE = ROLE.staff,
    @SerializedName("status") var disable: Boolean = true,
    @SerializedName("deviceId") var deviceId: String = "",
    @SerializedName("accessToken") var accessToken: String = "",
) : Serializable

data class UserRes(
    @SerializedName("_id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("status") var status: Boolean = true,
    @SerializedName("role") var role: ROLE = ROLE.staff,
    @SerializedName("linkAvt") var linkAvt: String = Constant.AVATAR_DEFAULT,
    @SerializedName("deviceId") var deviceId: String = "",
) {
    constructor(user: User) : this() {
        this.id = user.id
        this.name = user.name
        this.email = user.email
        this.role = user.role
        this.deviceId = user.deviceId
        this.status = user.disable
        this.phoneNumber = user.phone
        this.linkAvt = user.avatar
    }
}