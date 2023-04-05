package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("_id") val id: String = "",
    @SerializedName("name") var name: String = "",

data class Category(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("industryName") val name: String = "",
    @SerializedName("createdAt") val createAt: String = "",
)

data class CategoryParam(
    @SerializedName("industryName") val name: String
)

sealed class Response<out R> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val message: String) : Response<Nothing>()
}

val Response<*>.succeeded
    get() = this is Response.Success && data != null