package net.fpoly.dailymart.view.add_staff

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.databinding.ActivityAddStaffBinding
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.view.login.LoginEvent
import net.fpoly.dailymart.view.main.MainActivity
import net.fpoly.dailymart.view.profile.ChangeRoleDialog

class AddStaffActivity : BaseActivity<ActivityAddStaffBinding>(ActivityAddStaffBinding::inflate) {

    private val viewModel: AddStaffViewModel by viewModels { AppViewModelFactory }
    private var role: ROLE = ROLE.staff
    private var mLoadingDialog: LoadingDialog? = null

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
        viewModel.addStaffSuccess.observe(this, {
            mLoadingDialog?.hideLoading()
            if (it) {
//                openActivity(MainActivity::class.java)
                finishAffinity()
            }
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

    private fun validateUser(name: String, email: String, phone: String) {
        if (name.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()) {
            viewModel.onEvent(AddStaffViewModel.UserEvent.OnNameUser(name), this)
            viewModel.onEvent(AddStaffViewModel.UserEvent.OnEmail(email), this)
            viewModel.onEvent(AddStaffViewModel.UserEvent.OnPhoneNumberChange(phone), this)
        }
    }

    private fun createUser() {
        val name = binding.edNameUser.text.toString()
        val email = binding.edEmailUser.text.toString()
        val phone = binding.edNumberUser.text.toString()
        validateUser(name, email, phone)
        val user = RegisterParam(
            name = name,
            password = phone,
            email = email,
            phoneNumber = phone,
            role = role,
            deviceId = "",
            linkAvt = Constant.AVATAR_DEFAULT
        )
        viewModel.postUser(user, this, this)
    }

    private fun setupBtnChangeRole() {
        binding.layoutRole.setOnClickListener {
            ChangeRoleDialog(this, ROLE.staff) {
                role = it
                binding.edRoleUser.text = it.value
            }.show()
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(this, c))
    }
}