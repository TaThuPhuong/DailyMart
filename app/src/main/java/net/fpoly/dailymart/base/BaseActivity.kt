package net.fpoly.dailymart.base

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.lifecycle.LifecycleObserver
import androidx.viewbinding.ViewBinding
import net.fpoly.dailymart.R

abstract class BaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) :
    AppCompatActivity(),
    LifecycleObserver {
    open lateinit var binding: B

    open fun setOnClickListener() {}
    protected abstract fun setupData()
    protected abstract fun setupObserver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, windowInsets ->
            window.decorView.updatePadding(bottom = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
            windowInsets
        }

        setContentView(binding.root)
        setOnClickListener()
        setupData()
        setupObserver()
    }
}

