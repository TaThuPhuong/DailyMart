package net.fpoly.dailymart.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
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

        window.navigationBarColor = Color.WHITE
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            val navBarHeight = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            findViewById<View>(android.R.id.content).setPadding(0, 0, 0, navBarHeight)
            windowInsetsController.let {
                it.isAppearanceLightStatusBars = true
                it.isAppearanceLightNavigationBars = true
            }
            windowInsets
        }

        setOnClickListener()
        setupData()
        setupObserver()
    }
}

