package net.fpoly.dailymart.data.model.param

import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import java.io.Serializable

data class UserModel(
    val status: Long,
    val message: String,
    val data: List<Datum>,
) : Serializable

data class Datum(
    val _id: String,
    val name: String,
    val status: Boolean,
    val role: ROLE,
    val email: String,
    val phoneNumber: String,
    val deviceId: String = "",
    val linkAvt: String = Constant.AVATAR_DEFAULT,
) : Serializable {
    override fun hashCode(): Int {
        val result = _id.hashCode()
        if (deviceId.isEmpty()) {
            result
        }
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Datum

        if (_id != other._id) return false
        if (name != other.name) return false
        if (status != other.status) return false
        if (role != other.role) return false
        if (email != other.email) return false
        if (phoneNumber != other.phoneNumber) return false
        if (deviceId != other.deviceId) return false
        if (linkAvt != other.linkAvt) return false

        return true
    }
}
