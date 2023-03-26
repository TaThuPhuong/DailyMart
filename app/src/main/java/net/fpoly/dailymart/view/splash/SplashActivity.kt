package net.fpoly.dailymart.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import com.google.firebase.FirebaseOptions
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivitySplashBinding
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.main.MainActivity
import net.fpoly.dailymart.view.on_boarding.OnBoardingActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        viewModel.loadSplash()
        SharedPref.insertUser(
            this,
            User(
                "1234",
                "admin",
                "",
                "admin@gmail.com",
                "0123456789",
                "12345678",
                ROLE.ADMIN.value,
                false,
                "123443423"
            )
        )
        Log.d("YingMing", "setupData:     ${FirebaseOptions.fromResource(this)?.apiKey}")
    }

    override fun setupObserver() {
        viewModel.loadingSplash.observe(this) {
            if (it >= 200) {
                if (SharedPref.getTimeOnApp(this) < 1) {
                    SharedPref.setTimeOnApp(this)
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