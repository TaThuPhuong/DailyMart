package net.fpoly.dailymart.view.reset_password

import android.util.Log
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.param.SendOtpParam
import net.fpoly.dailymart.databinding.ActivityResetPasswordBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange

class ResetPasswordActivity :
    BaseActivity<ActivityResetPasswordBinding>(ActivityResetPasswordBinding::inflate) {
    private val viewModel: ResetPasswordViewModel by viewModels { AppViewModelFactory }
    private var id: String? = null

    override fun setupData() {
        id = intent.getStringExtra("id")
        Log.e("Tuvm", "setUpNewPass: $id")
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.initLoadDialog(this)
        onEditTextChange()
        sendSetUpNewPass()
    }

    override fun setupObserver() {

    }

    private fun sendSetUpNewPass() {
        binding.tvSendNewPass.setOnClickListener {
            setUpNewPass()
        }
    }

    private fun setUpNewPass() {
        val otp = binding.edOTP.text.toString()
        val newPass = binding.edNewPass.text.toString()
        val confirmPass = binding.edPassConfirm.text.toString()
        val sendOtp = SendOtpParam(id = id!!, token = otp, newPassword = newPass)
        viewModel.sendOTP(sendOtp, this, this)
    }

    private fun onEditTextChange() {
        binding.edOTP.getTextOnChange {
            viewModel.onEvent(ResetPasswordViewModel.SetupEvent.OnOTP(it), this)
        }
        binding.edNewPass.getTextOnChange {
            viewModel.onEvent(ResetPasswordViewModel.SetupEvent.OnNewPass(it), this)
        }
        binding.edPassConfirm.getTextOnChange {
            viewModel.onEvent(ResetPasswordViewModel.SetupEvent.OnConfirm(it), this)
        }
    }

}