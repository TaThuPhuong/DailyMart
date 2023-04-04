package net.fpoly.dailymart.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import net.fpoly.dailymart.R

abstract class BaseDialog<B : ViewBinding>(
    mContext: Context,
    var bindingFactory: (LayoutInflater) -> B,
) : Dialog(mContext) {
    lateinit var binding: B

    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window!!.decorView.setBackgroundResource(android.R.color.transparent)
        val window = window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initData()
    }
}