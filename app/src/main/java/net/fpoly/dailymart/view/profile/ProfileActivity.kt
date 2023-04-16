package net.fpoly.dailymart.view.profile

import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityProfileBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.hide
import net.fpoly.dailymart.firbase.storege.Images
import net.fpoly.dailymart.utils.ImagesUtils
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate),
    View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModels { AppViewModelFactory }

    private var mUser: User? = null

    private var onChangeAvatar = false
    private var mLoadingDialog: LoadingDialog? = null

    override fun setOnClickListener() {
        binding.imvBack.setOnClickListener(this)
        binding.tvSave.setOnClickListener(this)
        binding.imvAvatar.setOnClickListener(this)
        binding.layoutRole.setOnClickListener(this)
        binding.layoutDisable.setOnClickListener(this)

    }

    override fun setupData() {
        mLoadingDialog = LoadingDialog(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mUser = SharedPref.getUser(this)
        mUser?.let {
            Glide.with(this).load(it.avatar).placeholder(R.drawable.img_avatar_default)
                .into(binding.imvAvatar)
            binding.tvRole.text = it.role.value
            binding.tvDisable.text = getDisable(it.disable)
            binding.edName.setText(it.name)
            binding.edEmail.setText(it.email)
            binding.edPhone.setText(it.phone)
        }
        setOnTextChange()
    }

    override fun setupObserver() {
        viewModel.message.observe(this) {
            if (it.isNotBlank()) {
                showToast(this, it)
            }
        }
        viewModel.updateSuccess.observe(this) {
            if (it) {
                SharedPref.insertUser(this, mUser!!)
                showToast(this, "Đã lưu")
            }
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.imvBack -> finish()
            binding.imvAvatar -> {
                ImagesUtils.checkPermissionPickImage(this, binding.imvAvatar) {
                    onChangeAvatar = true
                }
            }
            binding.tvSave -> {
                if (onChangeAvatar) {
                    Images.uploadImage(
                        binding.imvAvatar,
                        "Users",
                        mUser!!.id,
                        onSuccess = {
                            mUser = mUser?.copy(avatar = it)
                            viewModel.updateUser(mUser!!)
                        },
                        onFail = {
                            viewModel.updateUser(mUser!!)
                            showToast(this, "Update ảnh lỗi, vui lòng thử lại sau !")
                        })
                } else {
                    viewModel.updateUser(mUser!!)
                }
            }
        }
    }

    private fun setOnTextChange() {
        binding.edName.getTextOnChange {
            mUser = mUser?.copy(name = it)
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