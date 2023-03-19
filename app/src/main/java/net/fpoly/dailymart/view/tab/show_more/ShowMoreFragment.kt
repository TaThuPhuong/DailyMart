package net.fpoly.dailymart.view.tab.show_more

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.ShowMoreFragmentBinding
import net.fpoly.dailymart.view.change_password.ChangePasswordActivity
import net.fpoly.dailymart.view.message.MessageActivity
import net.fpoly.dailymart.view.profile.ProfileActivity
import net.fpoly.dailymart.view.staff.StaffActivity

class ShowMoreFragment : BaseFragment<ShowMoreFragmentBinding>(ShowMoreFragmentBinding::inflate),
    View.OnClickListener {

    private val viewModel: ShowMoreViewModel by viewModels { AppViewModelFactory }
    override fun setOnClickListener() {
        binding.imvAvatar.setOnClickListener(this)
        binding.layoutProfile.setOnClickListener(this)
        binding.layoutChangePassword.setOnClickListener(this)
        binding.layoutMessage.setOnClickListener(this)
        binding.layoutReport.setOnClickListener(this)
        binding.layoutStaff.setOnClickListener(this)
        binding.layoutUpdateData.setOnClickListener(this)
        binding.layoutLogOut.setOnClickListener(this)
    }

    override fun setupData() {

    }

    override fun setupObserver() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imvAvatar, binding.layoutProfile -> openActivity(ProfileActivity::class.java)
            binding.layoutChangePassword -> openActivity(ChangePasswordActivity::class.java)
            binding.layoutMessage -> openActivity(MessageActivity::class.java)
            binding.layoutReport -> {}
            binding.layoutStaff -> openActivity(StaffActivity::class.java)
            binding.layoutUpdateData -> {}
            binding.layoutLogOut -> {}
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(mContext, c))
    }
}