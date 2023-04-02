package net.fpoly.dailymart.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
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

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
            view.updatePadding(insets.left, 0, insets.right, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        setContentView(binding.root)
        setOnClickListener()
        setupObserver()
        setupData()
    }


}

