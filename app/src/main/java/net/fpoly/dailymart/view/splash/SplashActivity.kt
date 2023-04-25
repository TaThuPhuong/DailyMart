package net.fpoly.dailymart.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivitySplashBinding
import net.fpoly.dailymart.firbase.database.BankDao
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.login.LoginActivity
import net.fpoly.dailymart.view.main.MainActivity
import net.fpoly.dailymart.view.on_boarding.OnBoardingActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels { AppViewModelFactory }
    private lateinit var bindingData: ActivitySplashBinding
    private var active = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
            view.updatePadding(insets.left, 0, insets.right, insets.bottom)
            view.updatePadding(0, 0, 0, 0)
            WindowInsetsCompat.CONSUMED
        }
        bindingData = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(bindingData.root)
        viewModel.loadSplash()
        BankDao.getBankInfo {
            it?.let { bank ->
                SharedPref.setBankInfo(this, bank)
            }
        }
        viewModel.checkActive(this) {
            active = it
        }

        viewModel.loadingSplash.observe(this) {
            if (it >= 200) {
                if (SharedPref.getTimeOnApp(this) < 1) {
                    SharedPref.setTimeOnApp(this)
                    openActivity(OnBoardingActivity::class.java)
                } else {
                    SharedPref.setTimeOnApp(this)
                    if (SharedPref.getUser(this@SplashActivity).id.isEmpty() || !active) {
                        openActivity(LoginActivity::class.java)
                    } else {
                        openActivity(MainActivity::class.java)
                    }
                }
            }
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(this, c))
        finishAffinity()
    }
}