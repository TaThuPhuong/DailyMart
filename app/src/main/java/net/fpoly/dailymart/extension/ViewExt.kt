package net.fpoly.dailymart.extension

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import net.fpoly.dailymart.data.model.param.InvoiceParam

fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<String>
) {
    snackbarEvent.observe(
        lifecycleOwner
    ) { event ->
        showSnackbar(event)
    }
}

fun View.showSnackbar(snackbarText: String) {
    Snackbar.make(this, snackbarText, Snackbar.LENGTH_SHORT).show()
}

@BindingAdapter("visibilityRefund")
fun View.setVisibility(invoice: InvoiceParam) {
    this.visibility = if(invoice.isRefundEmpty()) View.VISIBLE else View.GONE
}
