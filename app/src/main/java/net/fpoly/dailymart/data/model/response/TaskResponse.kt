package net.fpoly.dailymart.data.model.response

import net.fpoly.dailymart.data.model.Task

data class TaskResponse(
    var status: Int = 0,
    var message: String = "",
    var data: List<Task>,
)
