package net.fpoly.dailymart.view.login

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityLoginBinding
import net.fpoly.dailymart.view.forget_password.ForgetPasswordActivity
import net.fpoly.dailymart.view.main.MainActivity
import net.fpoly.dailymart.view.register.RegisterActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate),
    View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        setOnClickListener()
    }

    override fun setupObserver() {

    }

    private fun setOnClickListener() {
        binding.imvShowPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.layoutRegister.setOnClickListener(this)
        binding.tvForgetPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imvShowPassword -> viewModel.onEvent(LoginEvent.ShowPassword)
            binding.btnLogin -> openActivity(MainActivity::class.java)
            binding.layoutRegister -> openActivity(RegisterActivity::class.java)
            binding.tvForgetPassword -> openActivity(ForgetPasswordActivity::class.java)
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(this, c))
        finish()
    }
}