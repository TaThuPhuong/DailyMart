package net.fpoly.dailymart.view.task

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
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.TaskParam
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.response.TaskResponse
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskViewModel(private val app: Application) : ViewModel() {

    private val TAG = "YingMing"
    private var mUser: User? = SharedPref.getUser(app)

    private val _role = MutableLiveData(false)
    val role: LiveData<Boolean> = _role

    private val _listTask = MutableLiveData<List<Task>>()
    val listTask: LiveData<List<Task>> = _listTask

    val hasTask = MutableLiveData(false)

    private var taskDeleteRecent: Task? = null

    private val server = ServerInstance.apiTask
    private val mToken = SharedPref.getAccessToken(app)
    private var mViewPosition = 0

    init {
        _role.value = mUser!!.role != ROLE.staff
        Log.d(TAG, "_role: ${mUser!!.role}")
    }

    fun getAllTask(position: Int) {
        mViewPosition = position
        server.getAllTask(mToken).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val type = object : TypeToken<TaskResponse>() {}.type
                    val res: TaskResponse = Gson().fromJson(response.body()?.string(), type)
                    Log.d(TAG, "onResponse: ${res.data}")
                    if (res.data.isNotEmpty()) {
                        if (position == 0) {
                            _listTask.value = res.data.filter { !it.finish }
                            hasTask.value = _listTask.value?.isEmpty()
                        } else {
                            _listTask.value = res.data.filter { it.finish }
                            hasTask.value = _listTask.value?.isEmpty()
                        }
                    }
                } else {
                    showToast(app, response.errorBody()?.string() ?: "")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onResponse: $t")
            }
        })
    }

    fun onFinish(task: Task) {
        server.updateTask(mToken, TaskParam(task), task.id)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }
            })
    }

    fun onRestore() {
        taskDeleteRecent?.let {
            server.insertTask(mToken, TaskParam(it)).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    getAllTask(mViewPosition)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }
            })
        }
    }

    fun onDeleteTask(task: Task) {
        taskDeleteRecent = task
        server.deleteTask(mToken, task.id).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    getAllTask(mViewPosition)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }
}