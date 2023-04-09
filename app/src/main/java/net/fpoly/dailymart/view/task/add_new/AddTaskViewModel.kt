package net.fpoly.dailymart.view.task.add_new

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.RetrofitInstance
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.time_extention.toDate
import net.fpoly.dailymart.repository.TaskRepository
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.utils.sendNotification
import net.fpoly.dailymart.view.task.task_detail.TaskDetail
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTaskViewModel(
    private val app: Application,
    private val taskRepo: TaskRepository,
    private val userRepo: UserRepository,
) :
    ViewModel() {

    private val TAG = "YingMing"

    private val mUser = SharedPref.getUser(app)
    private val mToken = SharedPref.getAccessToken(app)

    private val _task = MutableLiveData(TaskParam())
    val task: LiveData<TaskParam> = _task

    private val _taskValidate = MutableLiveData(false)
    val taskValidate: LiveData<Boolean> = _taskValidate

    private val _listUser = MutableLiveData<List<UserRes>?>()
    val listUser: LiveData<List<UserRes>?> = _listUser

    private val _deviceId = MutableLiveData("")
    val message = MutableLiveData<String>()
    val addSuccess = MutableLiveData<Boolean>(null)

    init {
        _task.value = _task.value?.copy(
            idCreator = mUser!!.id,
        )
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
                Log.e(TAG, "ReceiverChange: ${event.user.id}")
                _task.value = _task.value?.copy(
                    idReceiver = event.user.id,
                )
                _deviceId.value = event.user.deviceId
                checkValidate()
            }
            is AddTaskEvent.TimeStartChange -> {
                _task.value = _task.value?.copy()
                checkValidate()
            }
            is AddTaskEvent.TimeEndChange -> {
                _task.value = _task.value?.copy(
                    deadline = event.time
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
                viewModelScope.launch(Dispatchers.IO) {
                    _task.value?.let {
                        val res = taskRepo.insertTask(mToken, it)
                        Log.e(TAG, "_task: $it")
                        if (res.isSuccess()) {
                            sendNotification(
                                "Bạn vừa giao 1 nhiệm vụ mới",
                                _task.value!!.title,
                                _deviceId.value!!
                            )
                            message.postValue(res.message!!)
                            addSuccess.postValue(true)
                        } else {
                            message.postValue(res.message!!)
                            addSuccess.postValue(false)
                        }
                    }
                }
            }
        }
    }

    fun getAllUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = userRepo.getAllUser2(mToken)
            if (res.isSuccess()) {
                res.data?.let { users ->
                    _listUser.postValue(users.filter { it.role == ROLE.staff })
                }
            } else {
                message.postValue(res.message!!)
            }
        }
    }

    private fun checkValidate() {
        _taskValidate.value =
            !(_task.value?.title?.trim() == null || _task.value?.idReceiver == null || _task.value?.deadline == 0L)
    }
}

sealed class AddTaskEvent {
    data class TitleChange(val title: String) : AddTaskEvent()
    data class ReceiverChange(val user: UserRes) : AddTaskEvent()
    data class TimeStartChange(val time: Long) : AddTaskEvent()
    data class TimeEndChange(val time: Long) : AddTaskEvent()
    data class DescriptionChange(val des: String) : AddTaskEvent()
    object AddNew : AddTaskEvent()
}
