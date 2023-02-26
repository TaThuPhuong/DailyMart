package net.fpoly.dailymart.view.register

import android.content.Intent
import android.view.View
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityRegisterBinding
import net.fpoly.dailymart.view.login.LoginActivity
import net.fpoly.dailymart.view.main.MainActivity

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate),
    View.OnClickListener {

    override fun setupData() {
        setOnClickListener()
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
            binding.imvShowPassword -> {}
            binding.imvShowConfirmPassword -> {}
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(this, c))
        finish()
    }

}