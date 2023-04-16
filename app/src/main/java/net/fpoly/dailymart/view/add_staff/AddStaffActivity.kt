package net.fpoly.dailymart.view.add_staff

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.databinding.ActivityAddStaffBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.view.profile.ChangeRoleDialog

class AddStaffActivity : BaseActivity<ActivityAddStaffBinding>(ActivityAddStaffBinding::inflate) {

    private val viewModel: AddStaffViewModel by viewModels { AppViewModelFactory }
    private var mLoadingDialog: LoadingDialog? = null

    override fun setupData() {
        onEditTextChange()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.layoutRole.setOnClickListener {
            ChangeRoleDialog(this, ROLE.staff) {
                binding.edRoleUser.text = it.value
            }.show()
        }
        binding.tvSave.setOnClickListener {
            mLoadingDialog?.showLoading()
            viewModel.onEvent(UserEvent.CreateUser)
        }
        binding.imvBack.setOnClickListener { finish() }
    }

    override fun setupObserver() {
        viewModel.addStaffSuccess.observe(this) {
            mLoadingDialog?.hideLoading()
            if (it) {
                finish()
                showToast(this, "Thêm thành công")
            }
        }
        viewModel.message.observe(this) {
            if (it.isNotEmpty()) showToast(this, it)
        }
    }

    private fun onEditTextChange() {
        binding.edNameUser.getTextOnChange {
            viewModel.onEvent(UserEvent.OnNameChange(it))
        }
        binding.edNumberUser.getTextOnChange {
            viewModel.onEvent(UserEvent.OnPhoneNumberChange(it))
        }
        binding.edEmailUser.getTextOnChange {
            viewModel.onEvent(UserEvent.OnEmailChange(it))
        }
    }
}