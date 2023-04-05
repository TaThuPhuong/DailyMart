package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("_id") val id: String = "",
    @SerializedName("name") var name: String = "",
)

