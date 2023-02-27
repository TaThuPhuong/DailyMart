package net.fpoly.dailymart.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleObserver
import androidx.viewbinding.ViewBinding

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
        setContentView(binding.root)
//        StatusBarCompat().translucentStatusBar(this, true)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, _ ->
            WindowInsetsCompat.CONSUMED
        }
        setOnClickListener()
        setupData()
        setupObserver()
    }
}

