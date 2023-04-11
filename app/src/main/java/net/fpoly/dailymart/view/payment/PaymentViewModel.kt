package net.fpoly.dailymart.view.payment

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.data.model.param.InvoiceParam
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity

class PaymentViewModel : ViewModel() {

    val invoice = MutableLiveData<InvoiceParam>()

    fun getInvoice(activity: PaymentActivity) {
        with(activity) {
            val formIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    AddInvoiceExportActivity.TAG_FINAL_INVOICE,
                    InvoiceParam::class.java
                )
            } else {
                intent.getParcelableExtra(AddInvoiceExportActivity.TAG_FINAL_INVOICE)
            }
            formIntent?.also {
                invoice.value = it
            }
        }
    }


}