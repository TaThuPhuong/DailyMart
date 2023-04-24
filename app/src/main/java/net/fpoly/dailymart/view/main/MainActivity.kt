package net.fpoly.dailymart.view.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { AppViewModelFactory }
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            val navBarHeight =
                windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            binding.layoutBottomNavigationBackgroundConnerTop.setPadding(0, 0, 0, navBarHeight)
            windowInsetsController.let {
                it.isAppearanceLightStatusBars = true
                it.isAppearanceLightNavigationBars = true
            }
            windowInsets
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.setupCheckEvent(this)
        viewModel.openTabEvent.observe(this) {
            navController = findNavController(R.id.nav_host_fragment)
            if (navController.currentDestination?.id != it) {
                navController.navigate(it)
            }
        }
        checkNotification()
    }

    private fun checkNotification() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Yêu cầu cấp quyền gửi thông báo nếu chưa được cấp
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == navController.graph.startDestinationId) {
            finish()
        } else {
            navController.popBackStack(R.id.home_fragment, false)
            viewModel.backToHomeTab()
        }
    }

    companion object {
        const val MAIN_EVENT = "MAIN_EVENT"
    }
}