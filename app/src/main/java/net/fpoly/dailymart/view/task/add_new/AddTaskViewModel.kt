package net.fpoly.dailymart.view.task.add_new

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

class AddTaskViewModel(
    private val app: Application,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val mUser = SharedPref.getUser(app)

    private val _task = MutableLiveData(Task())
    val task: LiveData<Task> = _task

    private val _taskValidate = MutableLiveData(false)
    val taskValidate: LiveData<Boolean> = _taskValidate

    private val _listUser = MutableLiveData<List<User>>(ArrayList())
    val listUser: LiveData<List<User>> = _listUser

    init {
        _task.value = _task.value?.copy(
            idCreator = mUser.id
        )
        viewModelScope.launch {
            userRepository.getUserByRole(ROLE.STAFF)?.collect { users ->
                _listUser.value = users
            }
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
                    idReceiver = event.user.id
                )
                checkValidate()
            }
            is AddTaskEvent.TimeStartChange -> {
                _task.value = _task.value?.copy(
                    createAt = event.time
                )
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
                viewModelScope.launch {
                    _task.value?.let {
                        taskRepository.insertTask(it)
                    }
                }
            }
        }
    }

    private fun checkValidate() {
        _taskValidate.value =
            !(_task.value?.title?.trim() == null || _task.value?.idReceiver == null || _task.value?.createAt == 0L || _task.value?.deadline == 0L)
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
