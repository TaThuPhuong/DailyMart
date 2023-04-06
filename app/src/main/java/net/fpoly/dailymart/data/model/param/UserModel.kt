package net.fpoly.dailymart.data.model.param

import java.io.Serializable

data class UserModel (
    val status: Long,
    val message: String,
    val data: List<Datum>
) : Serializable

data class Datum(
    val id: String,
    val name: String,
    val status: Boolean,
    val role: String,
    val phoneNumber: String,
    val deviceID: String,
    val linkAvt: String
) : Serializable{
    override fun hashCode(): Int {
        var result = id.hashCode()
        if(deviceID.isNullOrEmpty()){
           result
        }
        return result
    }
}
