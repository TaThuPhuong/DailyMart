package net.fpoly.dailymart.view.tab.show_more

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.repository.NotificationRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class ShowMoreViewModel(
    private val app: Application,
    private val notificationRepo: NotificationRepository,
) : ViewModel() {

    private val mUser = SharedPref.getUser(app)
    val role = MutableLiveData(false)

    init {
        role.value = mUser.role != ROLE.staff
    }

    fun deleteNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepo.deleteSeenNotification()
        }
    }
}