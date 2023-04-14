package net.fpoly.dailymart.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.R
import net.fpoly.dailymart.view.main.MainActivity.Companion.MAIN_EVENT
import net.fpoly.dailymart.view.payment.PaymentActivity.Companion.NEW_INVOICE_CREATE

class MainViewModel : ViewModel() {

    private val _openTabEvent = MutableLiveData(R.id.home_fragment)
    val openTabEvent: LiveData<Int> = _openTabEvent
    private val _tabCount: MutableLiveData<Int> = MutableLiveData(0)
    val tabCount: LiveData<Int> = _tabCount

    fun openTab(id: Int) {
        _tabCount.value = id
        _openTabEvent.value = when (id) {
            0 -> R.id.home_fragment
            1 -> R.id.receipt_fragment
            2 -> R.id.product_fragment
            3 -> R.id.show_more_fragment
            else -> {
                return
            }
        }
    }

    fun backToHomeTab() {
        _tabCount.value = 0
        _openTabEvent.value = R.id.home_fragment
    }

    fun setupCheckEvent(activity: MainActivity) {
        with(activity) {
            val event = intent.getStringExtra(MAIN_EVENT)
            if (event == NEW_INVOICE_CREATE) {
                openTab(1)
            }
        }
    }
}