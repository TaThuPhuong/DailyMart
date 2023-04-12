package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import net.fpoly.dailymart.utils.ROLE
import java.io.Serializable

data class User(
    @ColumnInfo(name = "id") @SerializedName("_id") val id: String = "",
    @ColumnInfo(name = "full_name") @SerializedName("name") var name: String = "",
    @ColumnInfo(name = "avatar") @SerializedName("linkAvt") var avatar: String = "",
    @ColumnInfo(name = "email") @SerializedName("email") var email: String = "",
    @ColumnInfo(name = "phone") @SerializedName("phoneNumber") var phone: String = "",
    @ColumnInfo(name = "role") @SerializedName("role") var role: ROLE = ROLE.staff,
    @ColumnInfo(name = "disable") @SerializedName("status") var disable: Boolean = true,
    @ColumnInfo(name = "device_id") @SerializedName("deviceId") var deviceId: String = "",
    @ColumnInfo(name = "info_bank") var infoBank: String? = null,
    @ColumnInfo(name = "accessToken") @SerializedName("accessToken") var accessToken: String = "",
) : Serializable

data class UserRes(
    @SerializedName("_id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("status") var status: Boolean = true,
    @SerializedName("role") var role: ROLE = ROLE.staff,
    @SerializedName("linkAvt") var linkAvt: String = "",
    @SerializedName("deviceId") var deviceId: String = "",
) {
    constructor(user: User) : this() {
        this.id = user.id
        this.name = user.name
        this.role = user.role
        this.deviceId = user.deviceId
        this.status = user.disable
        this.phoneNumber = user.phone
        this.linkAvt = user.avatar
    }
}