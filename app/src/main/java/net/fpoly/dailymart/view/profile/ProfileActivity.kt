package net.fpoly.dailymart.view.profile

import android.view.View
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityProfileBinding
import net.fpoly.dailymart.utils.ROLE

class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate),
    View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModels { AppViewModelFactory }

    override fun setOnClickListener() {
        binding.imvBack.setOnClickListener(this)
        binding.tvSave.setOnClickListener(this)
        binding.imvAvatar.setOnClickListener(this)
        binding.layoutRole.setOnClickListener(this)
        binding.layoutDisable.setOnClickListener(this)

    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupObserver() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imvBack -> finish()
            binding.tvSave -> {}
            binding.imvAvatar -> {}
            binding.layoutRole -> {
                ChangeRoleDialog(this, ROLE.staff) {
                    binding.tvRole.text = getRole(it)
                }.show()
            }
            binding.layoutDisable -> {
                ChangeDisableDialog(this,false){
                    binding.tvDisable.text = getDisable(it)
                }.show()
            }
        }
    }

    private fun getRole(role: ROLE): String {
        return when (role) {
            ROLE.staff -> "Nhân viên"
            ROLE.manager -> "Quản lý"
            ROLE.admin -> "Admin"
        }
    }

    private fun getDisable(status: Boolean): String {
        return if (status) {
            "Vô hiệu hóa"
        } else {
            "Đang hoạt động"
        }
    }
}