package net.fpoly.dailymart.view.task

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.TaskParam
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.repository.TaskRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class TaskViewModel(private val app: Application, private val repo: TaskRepository) : ViewModel() {

    private val TAG = "YingMing"
    private var mUser: User? = SharedPref.getUser(app)

    private val _role = MutableLiveData(false)
    val role: LiveData<Boolean> = _role

    private val _listTask = MutableLiveData<List<Task>>()
    val listTask: LiveData<List<Task>> = _listTask

    val hasTask = MutableLiveData(false)

    private var taskDeleteRecent: Task? = null

    private val mToken = SharedPref.getAccessToken(app)
    private var mViewPosition = 0
    val message = MutableLiveData<String>()

    init {
        _role.value = mUser!!.role != ROLE.staff
    }

    fun getAllTask(position: Int) {
        mViewPosition = position
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getAllTask(mToken)
            if (res.isSuccess()) {
                if (position == 0) {
                    _listTask.postValue(res.data?.filter { !it.finish }
                        ?.sortedByDescending { it.createAt })
                    hasTask.postValue(_listTask.value?.isEmpty())
                } else {
                    _listTask.postValue(res.data?.filter { it.finish }
                        ?.sortedByDescending { it.createAt })
                    hasTask.postValue(_listTask.value?.isEmpty())
                }
            } else {
                message.postValue(res.message!!)
            }
        }
    }

    fun onFinish(task: Task) {
        task.finish = true
        viewModelScope.launch(Dispatchers.Default) {
            val res = repo.updateTask(mToken, TaskParam(task), task.id)
            if (res.isSuccess()) {
                message.postValue(res.message!!)
                getAllTask(mViewPosition)
            } else {
                message.postValue(res.message!!)
            }
        }
    }

    fun onRestore() {
        taskDeleteRecent?.let {
            viewModelScope.launch(Dispatchers.Default) {
                val res = repo.insertTask(mToken, TaskParam(it))
                if (res.isSuccess()) {
                    message.postValue(res.message!!)
                    getAllTask(mViewPosition)
                    taskDeleteRecent = null
                } else {
                    message.postValue(res.message!!)
                }
            }
        }
    }

    fun onDeleteTask(task: Task, onDeleteSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.deleteTask(mToken, task.id)
            if (res.isSuccess()) {
                taskDeleteRecent = task
                message.postValue(res.message!!)
                getAllTask(mViewPosition)
                onDeleteSuccess()
            } else {
                message.postValue(res.message!!)
            }
        }
    }
}