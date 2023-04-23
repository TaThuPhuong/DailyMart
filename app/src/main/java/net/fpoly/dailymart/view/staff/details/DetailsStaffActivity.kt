package net.fpoly.dailymart.view.staff.details

import androidx.activity.viewModels
import com.bumptech.glide.Glide
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.databinding.ActivityDetailsStaffBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.firbase.storege.Images
import net.fpoly.dailymart.utils.ImagesUtils
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.view.profile.ChangeDisableDialog
import net.fpoly.dailymart.view.profile.ChangeRoleDialog

class DetailsStaffActivity :
    BaseActivity<ActivityDetailsStaffBinding>(ActivityDetailsStaffBinding::inflate) {
    private val viewModel: DetailStaffViewModel by viewModels { AppViewModelFactory }

    private var mUser: Datum? = null
    private var mRole: ROLE = ROLE.staff
    private var mStatus: Boolean = true
    private var isChangeAvatar = false
    private var mLoadingDialog: LoadingDialog? = null


    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mLoadingDialog = LoadingDialog(this)
        mUser = intent.getSerializableExtra("user") as Datum?
        binding.imvBack.setOnClickListener { finish() }
        mUser?.let {
            setData(it)
            viewModel.setUser(it)
        }
        onEditTextChange()

        binding.tvUpdate.setOnClickListener {
            mLoadingDialog?.showLoading()
            if (isChangeAvatar) {
                Images.uploadImage(binding.imvAvatar, "Users", mUser!!._id,
                    onSuccess = {
                        viewModel.onEvent(UpdateEvent.OnUpdate(it))
                    },
                    onFail = {
                        viewModel.onEvent(UpdateEvent.OnUpdate(null))
                        showToast(this, "Hình nền vẫn chưa được thay đổi")
                    })
            } else {
                viewModel.onEvent(UpdateEvent.OnUpdate(null))
            }
        }
//        binding.imvAvatar.setOnClickListener {
//            ImagesUtils.checkPermissionPickImage(this, binding.imvAvatar) {
//                isChangeAvatar = true
//            }
//        }
        setupBtnChangeRole()
        setupBtnChangeStatus()
    }

    override fun setupObserver() {
        viewModel.updateSuccess.observe(this) {
            mLoadingDialog?.hideLoading()
            if (it) {
                showToast(this, "Cập nhập thành công")
                finish()
            }
        }
        viewModel.message.observe(this) {
            if (it.isNotEmpty()) showToast(this, it)
        }
    }

    private fun setupBtnChangeRole() {
        binding.layoutRole.setOnClickListener {
            ChangeRoleDialog(this, mRole) {
                viewModel.onEvent(UpdateEvent.OnChangeRole(it))
                mRole = it
                binding.edRole.text = mRole.value
            }.show()
        }
    }

    private fun onEditTextChange() {
        binding.edName.getTextOnChange {
            viewModel.onEvent(UpdateEvent.OnNameChange(it))
        }
        binding.edPhone.getTextOnChange {
            viewModel.onEvent(UpdateEvent.OnPhoneNumberChange(it))
        }
        binding.edEmail.getTextOnChange {
            viewModel.onEvent(UpdateEvent.OnEmailChange(it))
        }
    }

    private fun setupBtnChangeStatus() {
        binding.layoutDisable.setOnClickListener {
            ChangeDisableDialog(this, mStatus) {
                viewModel.onEvent(UpdateEvent.OnChangeStatus(it))
                mStatus = it
                binding.edStatus.text = getStatus(it)
            }.show()
        }
    }

    private fun setData(user: Datum) {
        binding.edName.setText(user.name)
        binding.edEmail.setText(user.email)
        binding.edPhone.setText(user.phoneNumber)
        binding.edRole.text = user.role.value
        binding.edStatus.text = getStatus(user.status)
        Glide.with(this).load(user.linkAvt).placeholder(R.drawable.img_avatar_default)
            .error(R.drawable.img_avatar_default).into(binding.imvAvatar)
        mRole = user.role
        mStatus = user.status
    }

    private fun getStatus(status: Boolean): String = if (status) "Đang hoạt đông" else "Vô hiệu hóa"

}