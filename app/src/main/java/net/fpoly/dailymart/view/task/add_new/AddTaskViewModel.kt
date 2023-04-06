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
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.ResultData
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.TaskParam
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.extension.time_extention.toDate
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTaskViewModel(private val app: Application) : ViewModel() {

    private val TAG = "YingMing"

    private val mUser = SharedPref.getUser(app)
    private val mToken = SharedPref.getAccessToken(app)

    private val _task = MutableLiveData(TaskParam())
    val task: LiveData<TaskParam> = _task

    private val _taskValidate = MutableLiveData(false)
    val taskValidate: LiveData<Boolean> = _taskValidate

    private val _listUser = MutableLiveData<List<User>?>()
    val listUser: LiveData<List<User>?> = _listUser

    private val remoteUser = ServerInstance.apiUser
    private val remoteTask = ServerInstance.apiTask

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

    fun getAllUser() {
        remoteUser.getAllUser2(mToken).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
//                    val type = object : TypeToken<Result<List<User>>>() {}.type
//                    val res: ResultData<List<User>> =
//                        Gson().fromJson(response.body()?.string(), type)
                    Log.d(TAG, "_listUser: ${response.body()?.string()}")
//                    _listUser.value = res.result
                    Log.d(TAG, "_listUser: ${_listUser.value}")
                } else {
                    Log.d(TAG, "onFailure: ${response.errorBody()?.string()}")
                }
            }
        })
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
