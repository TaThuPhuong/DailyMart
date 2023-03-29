package net.fpoly.dailymart.view.add_staff

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityAddStaffBinding
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.view.profile.ChangeRoleDialog

class AddStaffActivity : BaseActivity<ActivityAddStaffBinding>(ActivityAddStaffBinding::inflate) {

    private val viewModel: AddStaffViewModel by viewModels { AppViewModelFactory }


    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupBtnChangeRole()
        setupBtnSave()
        binding.imvBack.setOnClickListener { finish() }
    }

    private fun setupBtnSave() {
        binding.tvSave.setOnClickListener {
            createUser()
        }
    }

    private fun createUser() {
        val name = binding.edNameUser.text.toString()
        val email = binding.edEmailUser.text.toString()
        val phone = binding.edNumberUser.text.toString()
        val user = User(name = name, email = email, phone = phone)

        viewModel.saveUser(user, this)
    }

    private fun setupBtnChangeRole() {
        binding.imvChangeRole.setOnClickListener {
            ChangeRoleDialog(this, ROLE.STAFF) {
                viewModel.newUser.value = viewModel.newUser.value!!.copy(role = it.value)
            }.show()
        }
    }


    override fun setupObserver() {

    }
}