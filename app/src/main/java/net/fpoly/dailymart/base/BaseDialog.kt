package net.fpoly.dailymart.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.viewbinding.ViewBinding

open class BaseDialog<B : ViewBinding>(
    mContext: Context,
    var bindingFactory: (LayoutInflater) -> B,
) : Dialog(mContext) {
    lateinit var binding: B
    open fun initData() {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
//        val window = window
//        window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initData()
    }
}