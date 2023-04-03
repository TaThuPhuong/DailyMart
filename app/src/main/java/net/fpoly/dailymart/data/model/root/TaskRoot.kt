package net.fpoly.dailymart.data.model.root

import com.google.gson.annotations.SerializedName
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User

data class TaskRoot(
    var status: Int = 0,
    var message: String = "",
    var data: Task
)
