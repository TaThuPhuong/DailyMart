package net.fpoly.dailymart.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBottomDialog<B : ViewBinding>(
    mContext: Context,
    var bindingFactory: (LayoutInflater) -> B,
) : BottomSheetDialog(mContext) {
    lateinit var binding: B
    protected abstract fun initData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        val window = window
        window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initData()
    }
}