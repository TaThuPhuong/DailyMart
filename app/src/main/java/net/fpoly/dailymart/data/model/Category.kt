package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Category(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("industryName") val name: String = "",
    @SerializedName("createdAt") val createAt: String = "",
    @SerializedName("status") val status: Boolean = true,

    ) : Serializable

data class CategoryParam(
    @SerializedName("industryName") val name: String,
    @SerializedName("status") val status: Boolean = true,
)

data class CategoryAddParam(
    @SerializedName("industryName") val name: String
)

sealed class Response<out R> {
    data class Success<T>(val data: T, val message: String, val page: Int = 1) : Response<T>()
    data class Error(val message: String) : Response<Nothing>()
}

val Response<*>.succeeded
    get() = this is Response.Success && data != null