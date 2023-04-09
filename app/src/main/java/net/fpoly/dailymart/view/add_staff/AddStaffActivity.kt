package net.fpoly.dailymart.view.add_staff

import android.widget.Toast
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.databinding.ActivityAddStaffBinding
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.view.login.LoginEvent
import net.fpoly.dailymart.view.main.MainActivity
import net.fpoly.dailymart.view.profile.ChangeRoleDialog

class AddStaffActivity : BaseActivity<ActivityAddStaffBinding>(ActivityAddStaffBinding::inflate) {

    private val viewModel: AddStaffViewModel by viewModels { AppViewModelFactory }
    private var role: ROLE = ROLE.staff

    override fun setupData() {
        onEditTextChange()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupBtnChangeRole()
        setupBtnSave()
        viewModel.initLoadDialog(context = this)
        binding.imvBack.setOnClickListener { finish() }
    }

    override fun setupObserver() {
        viewModel.user.observe(this, { user ->

        })
    }

    private fun setupBtnSave() {
        binding.tvSave.setOnClickListener {
            viewModel.onEvent(AddStaffViewModel.UserEvent.ValidateForm, this)
            createUser()
        }
    }

    private fun onEditTextChange() {
        binding.edNameUser.getTextOnChange {
            viewModel.onEvent(AddStaffViewModel.UserEvent.OnNameUser(it), this)
        }
        binding.edNumberUser.getTextOnChange {
            viewModel.onEvent(AddStaffViewModel.UserEvent.OnPhoneNumberChange(it), this)
        }
        binding.edEmailUser.getTextOnChange {
            viewModel.onEvent(AddStaffViewModel.UserEvent.OnEmail(it), this)
        }
    }

    private fun onValidateClick(name: String, email: String, phone: String) {
        if (name.trim().isEmpty()) {
            viewModel._validateName.value = "Không được để trống trường này"
            return
        }
    }

    private fun createUser() {
        val name = binding.edNameUser.text.toString()
        val email = binding.edEmailUser.text.toString()
        val phone = binding.edNumberUser.text.toString()
        onValidateClick(name, email, phone)
        val user = RegisterParam(
            name = name,
            password = phone,
            email = email,
            phoneNumber = phone,
            role = role,
            deviceId = "124234",
            linkAvt = "https//:abc.com"
        )
        viewModel.postUser(user, this)
    }

    private fun setupBtnChangeRole() {
        binding.imvChangeRole.setOnClickListener {
            ChangeRoleDialog(this, ROLE.staff) {
                role = it
                binding.edRoleUser.setText(it.value)
            }.show()
        }
    }
}