package net.fpoly.dailymart.view.forget_password

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.param.ForgotPass
import net.fpoly.dailymart.databinding.ActivityForgetPasswordBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.reset_password.ResetPasswordViewModel

class ForgetPasswordActivity :
    BaseActivity<ActivityForgetPasswordBinding>(ActivityForgetPasswordBinding::inflate) {

    private val viewModel: ForgetPassViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        send()
        viewModel.initLoadDialog(this)
        onEditTextChange()
        binding.imvBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setupObserver() {

    }

    private fun send() {
        binding.btnSend.setOnClickListener() {
            sendOTP()
        }
    }

    private fun onEditTextChange() {
        binding.edEmailPhone.getTextOnChange {
            viewModel.onEvent(ForgetPassViewModel.ForgotEvent.OnEmail(it), this)
        }
    }

    private fun sendOTP() {
        val mUser = SharedPref.getUser(this)
        val email = binding.edEmailPhone.text.toString()
        val changePassParam = ForgotPass(
            email
        )
        viewModel.sendOTP(changePassParam, this, this)
    }
}