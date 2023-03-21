package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier")
data class Supplier(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "phone") var phone: String = "",
)
