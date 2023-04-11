package net.fpoly.dailymart.view.forget_password

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityForgetPasswordBinding
import net.fpoly.dailymart.view.change_password.ChangePasswordViewModel

class ForgetPasswordActivity :
    BaseActivity<ActivityForgetPasswordBinding>(ActivityForgetPasswordBinding::inflate) {

    private val viewModel: ForgetPassViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {

    }

    override fun setupObserver() {

    }
}