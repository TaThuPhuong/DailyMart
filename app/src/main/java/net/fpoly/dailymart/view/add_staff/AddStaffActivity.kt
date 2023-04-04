package net.fpoly.dailymart.view.add_staff

import android.widget.Toast
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.databinding.ActivityAddStaffBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.view.profile.ChangeRoleDialog

class AddStaffActivity : BaseActivity<ActivityAddStaffBinding>(ActivityAddStaffBinding::inflate) {

    private val viewModel: AddStaffViewModel by viewModels { AppViewModelFactory }
    private var role: ROLE = ROLE.staff

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupBtnChangeRole()
        setupBtnSave()
        binding.imvBack.setOnClickListener { finish() }
    }

    override fun setupObserver() {
        viewModel.user.observe(this, { user ->

        })
    }

    private fun setupBtnSave() {
        binding.tvSave.setOnClickListener {
            createUser()
            showToast(applicationContext, "Success");
        }
    }

    private fun createUser() {
        val name = binding.edNameUser.text.toString()
        val email = binding.edEmailUser.text.toString()
        val phone = binding.edNumberUser.text.toString()
        val user = RegisterParam(
            name = name,
            password = phone,
            email = email,
            phoneNumber = phone,
            role = role,
            deviceId = "124234",
            linkAvt = "https//:abc.com"
        )
        viewModel.postUser(user)
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