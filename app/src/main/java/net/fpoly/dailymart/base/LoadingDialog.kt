package net.fpoly.dailymart.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import net.fpoly.dailymart.R


class LoadingDialog(val context: Context) {
    var dialog: Dialog? = null

    fun showLoading() {
        dialog = Dialog(context)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.dialog_loading)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog!!.show()
    }

    fun hideLoading() {
        dialog!!.dismiss()
    }
}