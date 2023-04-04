package net.fpoly.dailymart.view.task.add_new

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.RetrofitInstance
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.Data
import net.fpoly.dailymart.data.model.NotificationData
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.TaskParam
import net.fpoly.dailymart.extension.time_extention.toDate
import net.fpoly.dailymart.utils.SharedPref

class AddTaskViewModel(private val app: Application, ) : ViewModel() {

    private val TAG = "YingMing"

    private val mUser = SharedPref.getUser(app)

    private val _task = MutableLiveData(TaskParam())
    val task: LiveData<TaskParam> = _task

    private val _taskValidate = MutableLiveData(false)
    val taskValidate: LiveData<Boolean> = _taskValidate

    private val _listUser = MutableLiveData<List<User>>(ArrayList())
    val listUser: LiveData<List<User>> = _listUser

    init {
        _task.value = _task.value?.copy(
            idCreator = mUser!!.id,
        )
        viewModelScope.launch {
            val server = ServerInstance.apiUser

        }
    }

    fun onEvent(event: AddTaskEvent) {
        when (event) {
            is AddTaskEvent.TitleChange -> {
                _task.value = _task.value?.copy(
                    title = event.title
                )
                checkValidate()
            }
            is AddTaskEvent.ReceiverChange -> {
                _task.value = _task.value?.copy(
                    idReceiver = event.user.id,
                )
                checkValidate()
            }
            is AddTaskEvent.TimeStartChange -> {
                _task.value = _task.value?.copy(
                )
                checkValidate()
            }
            is AddTaskEvent.TimeEndChange -> {
                _task.value = _task.value?.copy(
                    deadline = event.time.toDate()
                )
                checkValidate()
            }
            is AddTaskEvent.DescriptionChange -> {
                _task.value = _task.value?.copy(
                    description = event.des
                )
                checkValidate()
            }
            is AddTaskEvent.AddNew -> {
            }
        }
    }

    private fun checkValidate() {
//        _taskValidate.value =
//            !(_task.value?.title?.trim() == null || _task.value?.idReceiver == null || _task.value?.createAt == 0L || _task.value?.deadline == 0L)
    }

    private fun sendNotification(task: Task) = CoroutineScope(Dispatchers.IO).launch {
        try {
//            val data = NotificationData(
//                Data("Nhiệm vụ mới", task.title, task.createAt),
//                task.
//            )
//            val response = RetrofitInstance.apiPutNotification.postNotification(data)
//            Log.d(TAG, "sendNotification: ${response.body()?.string()}")
//            if (response.isSuccessful) {
//                Log.d(TAG, "sendNotification: ${Gson().toJson(response)}")
//            } else {
//                Log.d(TAG, "sendNotification: ${response.errorBody().toString()}")
//            }
        } catch (e: Exception) {
            Log.e("YingMing", "sendNotification: $e")
        }
    }
}

sealed class AddTaskEvent {
    data class TitleChange(val title: String) : AddTaskEvent()
    data class ReceiverChange(val user: User) : AddTaskEvent()
    data class TimeStartChange(val time: Long) : AddTaskEvent()
    data class TimeEndChange(val time: Long) : AddTaskEvent()
    data class DescriptionChange(val des: String) : AddTaskEvent()
    object AddNew : AddTaskEvent()
}
