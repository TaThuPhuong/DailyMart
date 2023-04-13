package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_notification")
data class RecentNotification(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int? = null,
    @ColumnInfo(name = "time") var time: Long = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "message") var message: String = "",
    @ColumnInfo(name = "value") var value: String = "",
    @ColumnInfo(name = "seen") var seen: Boolean = false
)
