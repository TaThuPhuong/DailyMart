package net.fpoly.dailymart.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivitySplashBinding
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.main.MainActivity
import net.fpoly.dailymart.view.on_boarding.OnBoardingActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        viewModel.loadSplash()
    }

    override fun setupObserver() {
        viewModel.loadingSplash.observe(this) {
            if (it >= 200) {
                if (SharedPref.getTimeOnApp(this) < 1) {
                    openActivity(OnBoardingActivity::class.java)
                } else {
                    SharedPref.setTimeOnApp(this)
                    openActivity(MainActivity::class.java)
                }
            }
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(this, c))
        finish()
    }
}