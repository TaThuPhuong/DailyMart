package net.fpoly.dailymart.view.change_password

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.param.ChangePassParam
import net.fpoly.dailymart.databinding.ActivityChangePasswordBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.utils.SharedPref

class ChangePasswordActivity :
    BaseActivity<ActivityChangePasswordBinding>(ActivityChangePasswordBinding::inflate) {

    private val viewModel: ChangePasswordViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupSaveChangePass()
    }

    override fun setupObserver() {

    }

    private fun setupSaveChangePass() {
        binding.tvChangePass.setOnClickListener {
            changePass()
            showToast(applicationContext, "Success");
        }
    }

    private fun validateFiled() {
        binding.edNewPass
    }

    private fun changePass() {
        val mUser = SharedPref.getUser(this)
        val oldPass = binding.edOldPass.text.toString()
        val newPass = binding.edNewPass.text.toString()
        var conFirm = binding.edPassConfirm.text.toString()
        var changePassParam = ChangePassParam(
            id = mUser!!.id,
            phoneNumber = mUser.phone,
            newPass = newPass,
            oldPass = oldPass,
        )
        viewModel.changePass(changePassParam, this, this)
    }

}