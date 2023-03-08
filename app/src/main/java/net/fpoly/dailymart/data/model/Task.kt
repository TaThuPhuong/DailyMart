package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey @ColumnInfo(name = "createAt") val createAt: Long, // thời gian tạo khóa chính
    @ColumnInfo(name = "id_creator") val idCreator: String,       // id nhân viên tạo
    @ColumnInfo(name = "id_receiver") val idReceiver: String,      // id nhân viên nhận
    @ColumnInfo(name = "title") val title: String,                 // tiêu đề
    @ColumnInfo(name = "description") val description: String,     // mô tả
    @ColumnInfo(name = "deadline") var deadline: Long,              // hạn
    @ColumnInfo(name = "autoCreate") var autoCreate: Boolean = false,
    @ColumnInfo(name = "finish") var finish: Boolean = false,
    @ColumnInfo(name = "finish_time") var finishTime: Long = 0,
    @ColumnInfo(name = "comment") var comment: String = "",
)