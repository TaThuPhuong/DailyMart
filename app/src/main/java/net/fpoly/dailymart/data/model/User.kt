package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.fpoly.dailymart.utils.ROLE

@Entity(tableName = "user")
data class User(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "full_name") var name: String,
    @ColumnInfo(name = "avatar") var avatar: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "phone") var phone: String,
    @ColumnInfo(name = "password") var password: String = "",
    @ColumnInfo(name = "role") var role: ROLE = ROLE.STAFF,
    @ColumnInfo(name = "disable") var disable: Boolean = false,
    @ColumnInfo(name = "device_id") var deviceId: String = "",
    @ColumnInfo(name = "info_bank") var infoBank: String? = null,
)