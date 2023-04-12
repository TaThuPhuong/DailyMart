package net.fpoly.dailymart.view.task.task_detail

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.repository.TaskRepository
import net.fpoly.dailymart.utils.SharedPref
import java.text.SimpleDateFormat

class TaskDetailViewModel(private val app: Application, private val taskRepo: TaskRepository) :
    ViewModel() {

    private val _task = MutableLiveData(TaskDetail())
    val task: LiveData<TaskDetail> = _task

    private val mToken = SharedPref.getAccessToken(app)

    @SuppressLint("SimpleDateFormat")
    fun setTask(task: Task) {
        _task.postValue(
            TaskDetail(
                title = task.title,
                name = task.idReceiver.name,
                description = "Mô tả: ${task.description}",
                comment = task.comment,
                startTime = "Bắt đầu: ${SimpleDateFormat("HH:mm").format(task.createAt)}",
                endTime = "Kết thúc: ${SimpleDateFormat("HH:mm").format(task.deadline)}",
                finish = if (task.finish) "Đã hoàn thành" else if (task.deadline < System.currentTimeMillis()) "Quá hạn" else "Đang thực hiện"
            )
        )
    }

    fun getTaskById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = taskRepo.getTaskById(mToken, id)
            if (res.isSuccess()) {
                res.data?.let {
                    Log.e("YingMing", "getTaskById: $it")
                    setTask(it)
                }
            }
        }
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