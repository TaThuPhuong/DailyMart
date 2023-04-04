package net.fpoly.dailymart.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

data class Task(
    @SerializedName("_id") val id: String = "",
    @SerializedName("createdAt") var createAt: Date = Date(),
    @SerializedName("id_Creator") var idCreator: User = User(),
    @SerializedName("id_Receiver") var idReceiver: User = User(),
    @SerializedName("task_tittle") var title: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("deadline") var deadline: Long = 0,
    @SerializedName("finish") var finish: Boolean = false,
    @SerializedName("finish_time") var finishTime: Long = 0,
    @SerializedName("task_comment") var comment: String = "",
) {
    companion object {
         val listTitle = arrayListOf("Đang thực hiện", "Đã hoàn thành")
    }
}

data class TaskParam(
    @SerializedName("id_Creator") var idCreator: String = "",
    @SerializedName("id_Receiver") var idReceiver: String = "",
    @SerializedName("task_tittle") var title: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("deadline") var deadline: Date = Date(),
    @SerializedName("finish") var finish: Boolean = false,
    @SerializedName("finish_time") var finishTime: Long = 0,
    @SerializedName("task_comment") var comment: String = "",
)