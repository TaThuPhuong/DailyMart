package net.fpoly.dailymart.view.detailinvoice

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.utils.SharedPref

class DetailInvoiceViewModel(context: Context) : ViewModel() {

    val token = SharedPref.getAccessToken(context)
    val user = SharedPref.getUser(context)

    val showSnackbar = MutableLiveData<String>()
    val invoice = MutableLiveData<Invoice>()
    val isRefund = MutableLiveData(false)

    fun refundInvoice() {
        isRefund.value = true
    }

    fun getInvoice(activity: DetailInvoiceActivity) {
        with(activity) {
            val invoiceIntent = intent.getParcelableExtra<Invoice>(DetailInvoiceActivity.INVOICE)
            invoiceIntent.also {
                invoice.value = it
            }
        }
    }
}