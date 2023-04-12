package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Task(
    @SerializedName("_id") val id: String = "",
    @SerializedName("createdAt") var createAt: Date = Date(),
    @SerializedName("id_Creator") var idCreator: User = User(),
    @SerializedName("id_Receiver") var idReceiver: User = User(),
    @SerializedName("taskTitle") var title: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("deadline") var deadline: Long = 0,
    @SerializedName("finish") var finish: Boolean = false,
    @SerializedName("finish_time") var finishTime: Long = 0,
    @SerializedName("task_comment") var comment: String = "",
) : Serializable {
    companion object {
        val listTitle = arrayListOf("Đang thực hiện", "Đã hoàn thành")
    }
}

data class TaskSuccess(
    @SerializedName("_id") val id: String = "",
    @SerializedName("createdAt") var createAt: Date = Date(),
    @SerializedName("id_Creator") var idCreator: String = "",
    @SerializedName("id_Receiver") var idReceiver: String = "",
    @SerializedName("taskTitle") var title: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("deadline") var deadline: Long = 0,
    @SerializedName("finish") var finish: Boolean = false,
    @SerializedName("finish_time") var finishTime: Long = 0,
    @SerializedName("task_comment") var comment: String = "",
)

data class TaskParam(
    @SerializedName("id_Creator") var idCreator: String = "",
    @SerializedName("id_Receiver") var idReceiver: String = "",
    @SerializedName("task_tittle") var title: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("deadline") var deadline: Long = 0,
    @SerializedName("finish") var finish: Boolean = false,
    @SerializedName("finish_time") var finishTime: Long = 0,
    @SerializedName("task_comment") var comment: String = "",
) {
    constructor(task: Task) : this() {
        this.idCreator = task.idCreator.id
        this.idReceiver = task.idReceiver.id
        this.title = task.title
        this.description = task.description
        this.deadline = task.deadline
        this.finish = task.finish
        this.finishTime = task.finishTime
        this.comment = task.comment
    }
}