package net.fpoly.dailymart.view.tab.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.RecentNotification
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.extension.time_extention.startOfDay
import net.fpoly.dailymart.repository.InvoiceRepository
import net.fpoly.dailymart.repository.NotificationRepository
import net.fpoly.dailymart.repository.TaskRepository
import net.fpoly.dailymart.utils.SharedPref
import java.util.Calendar

class HomeViewModel(
    private val app: Application,
    private val taskRepo: TaskRepository,
    private val notificationRepo: NotificationRepository,
    private val invoiceRepo: InvoiceRepository
) :
    ViewModel() {

    private val TAG = "YingMing"
    val listNotification = MutableLiveData<List<RecentNotification>>(ArrayList())
    val isShowNotification = MutableLiveData(false)

    private val mToken = SharedPref.getAccessToken(app)
    val listTask = MutableLiveData<List<Task>>(ArrayList())

    val totalInvoice = MutableLiveData("")

    fun getAllNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            listNotification.postValue(
                notificationRepo.getNotificationSeen().sortedByDescending { it.time })
        }
    }

    fun onClickNotification() {
        isShowNotification.postValue(!isShowNotification.value!!)
    }

    fun onClickDelete() {
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepo.deleteSeenNotification()
            onClickNotification()
            getAllNotification()
        }
    }

    fun getAllTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = taskRepo.getAllTask(mToken)
            if (res.isSuccess()) {
                listTask.postValue(res.data!!.sortedByDescending { it.updatedAt.time })
            }
        }
    }

    fun getInvoiceToday() {
        viewModelScope.launch(Dispatchers.IO) {
            val today = Calendar.getInstance().startOfDay().timeInMillis
            when (val res = invoiceRepo.getInvoices(mToken)) {
                is Response.Error -> {}
                is Response.Success -> {
                    val listInvoice = res.data.filter { it.createAt >= today }
                    totalInvoice.postValue(listInvoice.size.toString() +" phiếu")
                    Log.e(TAG, "getInvoiceToday: ")
                }
            }
        }
    }
}