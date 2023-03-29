package net.fpoly.dailymart.view.staff.details

import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityDetailsStaffBinding
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.view.profile.ChangeDisableDialog
import net.fpoly.dailymart.view.profile.ChangeRoleDialog
import net.fpoly.dailymart.view.staff.StaffViewModel

class DetailsStaffActivity :
    BaseActivity<ActivityDetailsStaffBinding>(ActivityDetailsStaffBinding::inflate) {
    private val viewModel: StaffViewModel by viewModels { AppViewModelFactory }
    private var mUser: User? = null
    private var role: String? = ""
    private var status: Boolean = true

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.imvBack.setOnClickListener { finish() }
        setData()
        binding.tvUpdate.setOnClickListener {
            updateUser()
            Toast.makeText(this, "Thanh Cong", Toast.LENGTH_SHORT).show();
            onBackPressed()
        }
        setupBtnChangeRole()
        setupBtnChangeStatus()
    }

    private fun updateUser() {
        val name = binding.edName.text.toString()
        val email = binding.edEmail.text.toString()
        val phone = binding.edNumber.text.toString()
        role = binding.edRole.text.toString()
        val user = User(
            id = mUser!!.id,
            name = name,
            email = email,
            phone = phone,
            role = role!!,
            disable = status
        )

        viewModel.saveUser(user, this)
    }

    private fun setupBtnChangeRole() {
        binding.imvChangeRole.setOnClickListener {
            ChangeRoleDialog(this, mRole = ROLE.STAFF) {
                if (it.value == "STAFF") {
                    binding.edRole.setText("Nhân viên")
                } else {
                    binding.edRole.setText("Quản lý")
                }
                role = it.value
            }.show()
        }
    }

    private fun setupBtnChangeStatus() {
        binding.imvChangeStatus.setOnClickListener {
            ChangeDisableDialog(this, mStatus = false) {
                if (it) {
                    binding.edStatus.setText("Vô hiệu hóa")
                } else {
                    binding.edStatus.setText("Đang hoạt động")
                }
                status = it
            }.show()
        }
    }

    private fun setData() {
        mUser = intent.getSerializableExtra("user") as User
        mUser?.let {
            binding.edName.setText(mUser!!.name)
            binding.edEmail.setText(mUser!!.email)
            binding.edNumber.setText(mUser!!.phone)
            if (mUser!!.role == "STAFF") {
                binding.edRole.setText("Nhân viên")
            } else {
                binding.edRole.setText("Quản lý")
            }
            if (mUser!!.disable) {
                binding.edStatus.setText("Vô hiệu hóa")
            } else {
                binding.edStatus.setText("Hoạt động")
            }
            Log.e("tuvm", "setupObserver: $mUser");
        }
    }

    override fun setupObserver() {
        viewModel.listUser
    }

}