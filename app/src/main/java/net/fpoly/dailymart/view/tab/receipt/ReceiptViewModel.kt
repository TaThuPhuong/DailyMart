package net.fpoly.dailymart.view.tab.receipt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReceiptViewModel : ViewModel() {

    private val _openBill = MutableLiveData(true)
    val openBill: LiveData<Boolean> = _openBill
    fun onOpenTab(id: Int) {
        when (id) {
            1 -> _openBill.value = true
            2 -> _openBill.value = false
        }
    }

}