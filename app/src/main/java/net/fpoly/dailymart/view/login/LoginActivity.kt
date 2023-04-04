package net.fpoly.dailymart.view.login

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityLoginBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.view.forget_password.ForgetPasswordActivity
import net.fpoly.dailymart.view.main.MainActivity
import net.fpoly.dailymart.view.register.RegisterActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate),
    View.OnClickListener {

    private val TAG = "YingMing"

    private val viewModel: LoginViewModel by viewModels { AppViewModelFactory }

    override fun setOnClickListener() {
        binding.imvShowPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvForgetPassword.setOnClickListener(this)
    }

    override fun setupData() {
        onEditTextChange()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.initLoadDialog(this)
    }

    override fun setupObserver() {
        viewModel.loginSuccess.observe(this) {
            if (it) {
                openActivity(MainActivity::class.java)
                finishAffinity()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imvShowPassword -> viewModel.onEvent(LoginEvent.ShowPassword)
            binding.btnLogin -> viewModel.onEvent(LoginEvent.Login)
            binding.tvForgetPassword -> openActivity(ForgetPasswordActivity::class.java)
        }
    }

    private fun onEditTextChange() {
        binding.edEmailPhone.getTextOnChange {
            viewModel.onEvent(LoginEvent.OnPhoneNumberChange(it))
        }
        binding.edPassword.getTextOnChange {
            viewModel.onEvent(LoginEvent.OnPasswordChange(it))
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(this, c))
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}