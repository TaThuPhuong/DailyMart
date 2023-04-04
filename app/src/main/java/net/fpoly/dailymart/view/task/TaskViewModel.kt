package net.fpoly.dailymart.view.task

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.Task
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

    private val _listTask = MutableLiveData<List<Task>>(ArrayList())
    val listTask: LiveData<List<Task>> = _listTask

    private var taskDeleteRecent: Task? = null

    private val server = ServerInstance.apiTask

    init {
        _role.value = mUser!!.role != ROLE.staff
    }

    fun getAllTask(position: Int) {
        server.getAllTask(SharedPref.getAccessToken(app)).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()?.string()}")
//                    val type = object : TypeToken<TaskResponse>() {}.type
//                    val res: TaskResponse = Gson().fromJson(response.body()?.string(), type)
//                    if (position == 0) {
//                        _listTask.value = res.data.filter { !it.finish }
//                    } else {
//                        _listTask.value = res.data.filter { it.finish }
//                    }

                } else {
                    showToast(app, response.errorBody()?.string() ?: "")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })

    }

    fun onFinish(task: Task) {

    }

    fun onRestore() {
        viewModelScope.launch {
            taskDeleteRecent = null
        }
    }

    fun onDeleteTask(task: Task) {
        taskDeleteRecent = task

    }
}