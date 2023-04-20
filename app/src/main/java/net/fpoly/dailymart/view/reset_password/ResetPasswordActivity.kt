package net.fpoly.dailymart.view.reset_password

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.param.SendOtpParam
import net.fpoly.dailymart.databinding.ActivityResetPasswordBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.view.login.LoginActivity

class ResetPasswordActivity :
    BaseActivity<ActivityResetPasswordBinding>(ActivityResetPasswordBinding::inflate) {
    private val viewModel: ResetPasswordViewModel by viewModels { AppViewModelFactory }
    private var id: String? = null
    val TAG = "Tuvm"
    private var mLoadingDialog: LoadingDialog? = null

    override fun setupData() {
        id = intent.getStringExtra("id")
        Log.e("Tuvm", "setUpNewPass: $id")
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.initLoadDialog(this)
        mLoadingDialog = LoadingDialog(this)
        binding.imvShowPasswordConfirm.setOnClickListener() {
            viewModel.onEvent(ResetPasswordViewModel.SetupEvent.OnPassConfirm, applicationContext)
        }
        binding.imvShowPasswordNew.setOnClickListener() {
            viewModel.onEvent(ResetPasswordViewModel.SetupEvent.OnPassNew, applicationContext)
        }
        binding.imvBack.setOnClickListener() {
            onBackPressed()
        }
        onEditTextChange()
        sendSetUpNewPass()
    }

    override fun setupObserver() {
        viewModel.updateSuccess.observe(this) {
            mLoadingDialog?.hideLoading()
            if (it) {
                showToast(this, "Cập nhập thành công")
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
        viewModel.message.observe(this) {
            if (it.isNotEmpty()) showToast(this, it)
        }
    }

    private fun sendSetUpNewPass() {
        binding.tvSendNewPass.setOnClickListener {
            viewModel.onEvent(ResetPasswordViewModel.SetupEvent.ValidateForm, applicationContext)
        }
    }

//    private fun setUpNewPass() {
//        val otp = binding.edOTP.text.toString()
//        val newPass = binding.edNewPass.text.toString()
//        val confirmPass = binding.edPassConfirm.text.toString()
//        val sendOtp = SendOtpParam(id = id!!, token = otp, newPassword = newPass)
//        viewModel.sendOTP(sendOtp)
//    }

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