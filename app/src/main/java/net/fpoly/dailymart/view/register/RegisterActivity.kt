package net.fpoly.dailymart.view.register

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityRegisterBinding
import net.fpoly.dailymart.view.login.LoginActivity
import net.fpoly.dailymart.view.main.MainActivity

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate),
    View.OnClickListener {

    private val viewModel: RegisterViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        setOnClickListener()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupObserver() {

    }

    private fun setOnClickListener() {
        binding.ccp.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.imvShowPassword.setOnClickListener(this)
        binding.imvShowConfirmPassword.setOnClickListener(this)
        binding.layoutLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ccp -> {}
            binding.btnRegister -> openActivity(MainActivity::class.java)
            binding.layoutLogin -> openActivity(LoginActivity::class.java)
            binding.imvShowPassword -> viewModel.onEvent(RegisterEvent.ShowPassword)
            binding.imvShowConfirmPassword -> viewModel.onEvent(RegisterEvent.ShowConfirmPassword)
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(this, c))
        finish()
    }

}