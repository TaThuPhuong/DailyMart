package net.fpoly.dailymart.data.model.param

import com.google.gson.annotations.SerializedName
import java.util.*

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
