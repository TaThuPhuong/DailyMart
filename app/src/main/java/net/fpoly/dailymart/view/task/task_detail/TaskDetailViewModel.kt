package net.fpoly.dailymart.view.task.task_detail

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.data.model.Task
import java.text.SimpleDateFormat

class TaskDetailViewModel : ViewModel() {

    private val _task = MutableLiveData(TaskDetail())
    val task: LiveData<TaskDetail> = _task

    @SuppressLint("SimpleDateFormat")
    fun setTask(task: Task) {
        _task.value = TaskDetail(
            title = task.title,
            name = task.idReceiver.name,
            description = "Mô tả: ${task.description}",
            comment = task.comment,
            startTime = "Bắt đầu: ${SimpleDateFormat("HH:mm").format(task.createAt)}",
            endTime = "Kết thúc: ${SimpleDateFormat("HH:mm").format(task.deadline)}",
            finish = if (task.finish) "Đã hoàn thành" else if (task.deadline < System.currentTimeMillis()) "Quá hạn" else "Đang thực hiện"
        )
    }
}

data class TaskDetail(
    var id: String = "",
    var title: String = "",
    var name: String = "",
    var description: String = "",
    var startTime: String = "",
    var comment: String = "",
    var endTime: String = "",
    var finish: String = ""
)