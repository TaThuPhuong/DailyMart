package net.fpoly.dailymart.view.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel by viewModels<MainViewModel> { AppViewModelFactory }
    private lateinit var navController: NavController

    override fun setupData() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun setupObserver() {
        viewModel.openTabEvent.observe(this) {
            navController = findNavController(R.id.nav_host_fragment)
            if (navController.currentDestination?.id != it) {
                navController.navigate(it)
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
}