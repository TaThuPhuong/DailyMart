package net.fpoly.dailymart.view.tab.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.RecentNotification
import net.fpoly.dailymart.repository.NotificationRepository
import net.fpoly.dailymart.repository.TaskRepository

class HomeViewModel(
    private val app: Application,
    private val taskRepo: TaskRepository,
    private val notificationRepo: NotificationRepository
) :
    ViewModel() {

    val listNotification = MutableLiveData<List<RecentNotification>>(ArrayList())
    val isShowNotification = MutableLiveData(false)
    fun getAllNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            listNotification.postValue(
                notificationRepo.getNotificationSeen().sortedByDescending { it.time })
        }
    }

    fun onClickNotification() {
        isShowNotification.value = !isShowNotification.value!!
    }

    fun onClickDelete() {
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepo.deleteSeenNotification()
            getAllNotification()
        }
    }

    fun getAllTask(){

    }

}