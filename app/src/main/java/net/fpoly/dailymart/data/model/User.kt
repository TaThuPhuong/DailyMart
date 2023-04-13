package net.fpoly.dailymart.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import java.io.Serializable

@Parcelize
data class User(
    @ColumnInfo(name = "id") @SerializedName("_id") val id: String = "",
    @ColumnInfo(name = "full_name") @SerializedName("name") var name: String = "",
    @ColumnInfo(name = "avatar") @SerializedName("linkAvt") var avatar: String = Constant.AVATAR_DEFAULT,
    @ColumnInfo(name = "email") @SerializedName("email") var email: String = "",
    @ColumnInfo(name = "phone") @SerializedName("phoneNumber") var phone: String = "",
    @ColumnInfo(name = "role") @SerializedName("role") var role: ROLE = ROLE.staff,
    @ColumnInfo(name = "disable") @SerializedName("status") var disable: Boolean = true,
    @ColumnInfo(name = "device_id") @SerializedName("deviceId") var deviceId: String = "",
    @ColumnInfo(name = "info_bank") var infoBank: String? = null,
    @ColumnInfo(name = "accessToken") @SerializedName("accessToken") var accessToken: String = "",
) : Serializable, Parcelable

data class UserRes(
    @SerializedName("_id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("status") var status: Boolean = true,
    @SerializedName("role") var role: ROLE = ROLE.staff,
    @SerializedName("linkAvt") var linkAvt: String = Constant.AVATAR_DEFAULT,
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