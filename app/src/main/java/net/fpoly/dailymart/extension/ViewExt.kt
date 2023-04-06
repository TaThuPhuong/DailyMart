package net.fpoly.dailymart.extension

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar

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
