package net.fpoly.dailymart.view.change_password

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.param.ChangePassParam
import net.fpoly.dailymart.databinding.ActivityChangePasswordBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.add_staff.AddStaffViewModel

class ChangePasswordActivity :
    BaseActivity<ActivityChangePasswordBinding>(ActivityChangePasswordBinding::inflate) {

    private val viewModel: ChangePasswordViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        onEditTextChange()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupSaveChangePass()
        binding.imvBack.setOnClickListener {
            onBackPressed()
        }
        viewModel.initLoadDialog(this)
    }

    override fun setupObserver() {

    }

    private fun setupSaveChangePass() {
        binding.tvChangePass.setOnClickListener {
            changePass()
        }
    }

    private fun onEditTextChange() {
        binding.edOldPass.getTextOnChange {
            viewModel.onEvent(ChangePasswordViewModel.UserEvent.OnOldPass(it), this)
        }
        binding.edNewPass.getTextOnChange {
            viewModel.onEvent(ChangePasswordViewModel.UserEvent.OnNewPass(it), this)
        }
        binding.edPassConfirm.getTextOnChange {
            viewModel.onEvent(ChangePasswordViewModel.UserEvent.OnConfirm(it), this)
        }
    }

    private fun changePass() {
        val mUser = SharedPref.getUser(this)
        val oldPass = binding.edOldPass.text.toString()
        val newPass = binding.edNewPass.text.toString()
        var conFirm = binding.edPassConfirm.text.toString()
        val changePassParam = ChangePassParam(
            id = mUser!!.id,
            phoneNumber = mUser.phone,
            newPass = newPass,
            oldPass = oldPass,
        )
        viewModel.changePass(changePassParam, this, this)
    }

}