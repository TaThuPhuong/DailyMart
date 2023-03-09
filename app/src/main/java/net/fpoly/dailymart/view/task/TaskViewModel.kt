package net.fpoly.dailymart.view.task

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.repository.TaskRepository
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class TaskViewModel(
    private val app: Application,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) :
    ViewModel() {

    private val _tabAssignedOpen = MutableLiveData(true)
    val tabAssignedOpen: LiveData<Boolean> = _tabAssignedOpen

    private var mUser: User = SharedPref.getUser(app)

    private val _role = MutableLiveData(false)
    val role: LiveData<Boolean> = _role

    private val _listTask = MutableLiveData<List<Task>>(ArrayList())
    val listTask: LiveData<List<Task>> = _listTask

    private val _listUser = MutableLiveData<List<User>>(ArrayList())
    val listUser: LiveData<List<User>> = _listUser

    init {
        _role.value = mUser.role != ROLE.STAFF
        getListUser()
    }

    fun onOpenTab(id: Int) {
        when (id) {
            1 -> {
                if (_role.value == true) {
                    getAllListTaskByStatus(false)
                } else {
                    getListTaskByIdAndStatus(mUser.id, false)
                }
                _tabAssignedOpen.value = true
            }
            2 -> {
                if (_role.value == true) {
                    getAllListTaskByStatus(true)
                } else {
                    getListTaskByIdAndStatus(mUser.id, true)
                }
                _tabAssignedOpen.value = false
            }
        }
    }

    fun onDeleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    private fun getAllListTaskByStatus(finish: Boolean) {
        viewModelScope.launch {
            taskRepository.getTaskFinish(finish)?.collect { tasks ->
                _listTask.value = tasks.sortedByDescending { it.createAt }
            }
        }
    }

    private fun getListTaskByIdAndStatus(id: String, finish: Boolean) {
        viewModelScope.launch {
            taskRepository.getTaskByIdAndFinish(id, finish)?.collect { tasks ->
                _listTask.value = tasks.sortedByDescending { it.createAt }
            }
        }
    }

    private fun getListUser() {
        viewModelScope.launch {
            userRepository.getUserByRole(ROLE.STAFF)?.collect { users ->
                _listUser.value = users
            }
        }
    }
}