package net.fpoly.dailymart.view.change_password

import android.content.Intent
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityChangePasswordBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.utils.SharedPref

class ChangePasswordActivity :
    BaseActivity<ActivityChangePasswordBinding>(ActivityChangePasswordBinding::inflate) {

    private val viewModel: ChangePasswordViewModel by viewModels { AppViewModelFactory }
    private var mLoadingDialog: LoadingDialog? = null

    override fun setupData() {
        onEditTextChange()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mLoadingDialog = LoadingDialog(this)
        setupSaveChangePass()
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.imvShowPasswordOld.setOnClickListener {
            viewModel.onEvent(ChangePasswordViewModel.ChangeEvent.OnPassOld)
        }
        binding.imvShowPasswordNew.setOnClickListener {
            viewModel.onEvent(ChangePasswordViewModel.ChangeEvent.OnPassNew)
        }
        binding.imvShowPasswordConfirm.setOnClickListener {
            viewModel.onEvent(ChangePasswordViewModel.ChangeEvent.OnPassConfirm)
        }
    }

    override fun setupObserver() {
        viewModel.updateSuccess.observe(this) {
            mLoadingDialog?.hideLoading()
            if (it) {
                showToast(this, "Cập nhập thành công")
                finish()
            }
        }
        viewModel.message.observe(this) {
            mLoadingDialog?.hideLoading()
            if (it.isNotEmpty()) showToast(this, it)
        }
    }

    private fun setupSaveChangePass() {
        binding.tvChangePass.setOnClickListener {
            mLoadingDialog?.showLoading()
            viewModel.onEvent(
                ChangePasswordViewModel.ChangeEvent.ValidateForm
            )
        }
    }

    private fun onEditTextChange() {
        binding.edOldPass.getTextOnChange {
            viewModel.onEvent(ChangePasswordViewModel.ChangeEvent.OnOldPass(it))
        }
        binding.edNewPass.getTextOnChange {
            viewModel.onEvent(ChangePasswordViewModel.ChangeEvent.OnNewPass(it))
        }
        binding.edPassConfirm.getTextOnChange {
            viewModel.onEvent(ChangePasswordViewModel.ChangeEvent.OnConfirm(it))
        }
    }

//    private fun changePass() {
//        val mUser = SharedPref.getUser(this)
//        val oldPass = binding.edOldPass.text.toString()
//        val newPass = binding.edNewPass.text.toString()
//        var conFirm = binding.edPassConfirm.text.toString()
//        val changePassParam = ChangePassParam(
//            id = mUser.id,
//            phoneNumber = mUser.phone,
//            newPass = newPass,
//            oldPass = oldPass,
//        )
//        viewModel.changePass(changePassParam, this, this)
//    }

}