package net.fpoly.dailymart.view.task

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel(private val app: Application) : ViewModel() {

    private val _tabAssignedOpen = MutableLiveData(true)
    val tabAssignedOpen: LiveData<Boolean> = _tabAssignedOpen
    fun onOpenTab(id: Int) {
        when (id) {
            1 -> {
                _tabAssignedOpen.value = true
            }
            2 -> {
                _tabAssignedOpen.value = false
            }
        }
    }
}