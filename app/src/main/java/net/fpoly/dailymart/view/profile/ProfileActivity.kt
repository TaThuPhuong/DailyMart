package net.fpoly.dailymart.view.profile

import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityProfileBinding
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate),
    View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModels { AppViewModelFactory }

    private var mUser: User? = null

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
        mUser = SharedPref.getUser(this)
        mUser?.let {
            Glide.with(this).load(it.avatar).placeholder(R.drawable.ic_avatar_default)
                .into(binding.imvAvatar)
            binding.tvRole.text = it.role.value
            binding.tvDisable.text = getDisable(it.disable)
            binding.edName.setText(it.name)
            binding.edEmail.setText(it.email)
            binding.edPhone.setText(it.phone)
        }
    }

    override fun setupObserver() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imvBack -> finish()
            binding.imvAvatar -> {}
            binding.tvSave -> {}
        }
    }

    private fun getDisable(status: Boolean): String {
        return if (!status) {
            "Vô hiệu hóa"
        } else {
            "Đang hoạt động"
        }
    }
}