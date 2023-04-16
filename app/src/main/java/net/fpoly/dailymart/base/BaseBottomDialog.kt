package net.fpoly.dailymart.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import net.fpoly.dailymart.R

abstract class BaseBottomDialog<B : ViewBinding>(
    mContext: Context,
    var bindingFactory: (LayoutInflater) -> B,
) : BottomSheetDialog(mContext, R.style.BaseThemeBottomSheet) {
    lateinit var binding: B
    protected abstract fun initData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        initData()
    }
}