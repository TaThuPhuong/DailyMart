package net.fpoly.dailymart.view.task.task_edit

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.TaskParam
import net.fpoly.dailymart.repository.TaskRepository
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.utils.sendNotification
import net.fpoly.dailymart.view.task.task_detail.TaskDetail
import java.text.SimpleDateFormat

class TaskEditViewModel(private val app: Application, private val repo: TaskRepository) :
    ViewModel() {
    private val mToken = SharedPref.getAccessToken(app)
    private var mTask: Task? = null
    private val _task = MutableLiveData(TaskDetail())
    val task: LiveData<TaskDetail> = _task

    val taskParam = MutableLiveData(TaskParam())
    val message = MutableLiveData<String>()
    val updateTaskSuccess = MutableLiveData<Boolean>(null)

    @SuppressLint("SimpleDateFormat")
    fun setTask(task: Task) {
        mTask = task
        _task.value = TaskDetail(
            id = task.id,
            title = task.title,
            name = task.idReceiver.name,
            description = "Mô tả: ${task.description}",
            comment = task.comment,
            startTime = "Bắt đầu: ${SimpleDateFormat("HH:mm").format(task.createAt)}",
            endTime = "Kết thúc: ${SimpleDateFormat("HH:mm").format(task.deadline)}",
            finish = if (task.finish) "Đã hoàn thành" else if (task.deadline < System.currentTimeMillis()) "Quá hạn" else "Đang thực hiện"
        )
        taskParam.value = taskParam.value?.copy(
            idCreator = task.idCreator.id,
            idReceiver = task.idReceiver.id,
            title = task.title,
            description = task.description,
            deadline = task.deadline,
            finish = task.finish,
            finishTime = task.finishTime,
            comment = task.comment
        )
    }

    fun onEvent(event: EditEvent) {
        when (event) {
            is EditEvent.OnChangeDes -> {
                taskParam.value = taskParam.value?.copy(
                    description = event.description,
                )
            }
            is EditEvent.OnChangeEndTime -> {
                taskParam.value = taskParam.value?.copy(
                    deadline = event.time
                )
            }
            is EditEvent.OnChangeTitle -> {
                taskParam.value = taskParam.value?.copy(
                    title = event.title
                )
            }
            is EditEvent.OnFinishChange -> {
                taskParam.value = taskParam.value?.copy(
                    finish = event.b,
                    finishTime = 0
                )
                if (event.b) {
                    taskParam.value = taskParam.value?.copy(
                        finishTime = System.currentTimeMillis()
                    )
                }
            }
            EditEvent.OnSave -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val res = repo.updateTask(mToken, taskParam.value!!, task.value!!.id)
                    if (res.isSuccess()) {
                        message.postValue(res.message!!)
                        updateTaskSuccess.postValue(true)
                        mTask?.idReceiver?.deviceId?.let {
                            sendNotification(
                                "Đã chỉnh sửa",
                                mTask?.title ?: "",
                                mTask?.id!!, it
                            )
                        }
                    } else {
                        message.postValue(res.message!!)
                        updateTaskSuccess.postValue(false)
                    }
                }
            }
        }
    }
}

sealed class EditEvent {
    data class OnChangeTitle(val title: String) : EditEvent()
    data class OnChangeDes(val description: String) : EditEvent()
    data class OnChangeEndTime(val time: Long) : EditEvent()
    data class OnFinishChange(val b: Boolean) : EditEvent()
    object OnSave : EditEvent()
}