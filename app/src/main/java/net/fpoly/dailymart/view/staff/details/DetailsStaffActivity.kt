package net.fpoly.dailymart.view.staff.details

import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.data.model.param.UpdateParam
import net.fpoly.dailymart.data.model.param.UserModel
import net.fpoly.dailymart.databinding.ActivityDetailsStaffBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.firbase.storege.Images
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ImagesUtils
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.add_staff.AddStaffViewModel
import net.fpoly.dailymart.view.profile.ChangeDisableDialog
import net.fpoly.dailymart.view.profile.ChangeRoleDialog
import net.fpoly.dailymart.view.staff.StaffViewModel

class DetailsStaffActivity :
    BaseActivity<ActivityDetailsStaffBinding>(ActivityDetailsStaffBinding::inflate) {
    private val viewModel: StaffViewModel by viewModels { AppViewModelFactory }
    private var mUser: Datum? = null
    private var role: ROLE = ROLE.staff
    private var status: Boolean = true
    private var isChangeAvatar = false

    override fun setupData() {
        onEditTextChange()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.imvBack.setOnClickListener { finish() }
        setData()
        binding.tvUpdate.setOnClickListener {
            if (isChangeAvatar) {
                Images.uploadImage(binding.imvAvatar, "Users", mUser!!._id,
                    onSuccess = {
                        updateUser(it)
                    },
                    onFail = {
                        updateUser(null)
                        showToast(this, "Hình nền vẫn chưa được thay đổi")
                    })
            } else {
                updateUser(null)
            }
        }
        binding.imvAvatar.setOnClickListener {
            ImagesUtils.checkPermissionPickImage(this, binding.imvAvatar) {
                isChangeAvatar = true
            }
        }
        viewModel.initLoadDialog(context = this)
        setupBtnChangeRole()
        setupBtnChangeStatus()
    }

    override fun setupObserver() {

    }

    private fun updateUser(linkAvt: String?) {
        val name = binding.edName.text.toString()
        val email = binding.edEmail.text.toString()
        val phone = binding.edNumber.text.toString()
        val user = UpdateParam(
            _id = mUser!!._id,
            name = name,
            email = email,
            phoneNumber = phone,
            role = role,
            status = status,
            deviceId = mUser!!.deviceID,
            linkAvt = mUser!!.linkAvt
        )
        linkAvt?.let { user.linkAvt = it }
        viewModel.updateUser(mUser!!._id, user, this, this)
    }

    private fun setupBtnChangeRole() {
        binding.layoutRole.setOnClickListener {
            ChangeRoleDialog(this, mRole = ROLE.staff) {
                role = it
                binding.edRole.text = role.value
            }.show()
        }
    }

    private fun onEditTextChange() {
        binding.edName.getTextOnChange {
            viewModel.onEvent(StaffViewModel.UserEvent.OnNameUser(it), this)
        }
        binding.edNumber.getTextOnChange {
            viewModel.onEvent(StaffViewModel.UserEvent.OnPhoneNumberChange(it), this)
        }
        binding.edEmail.getTextOnChange {
            viewModel.onEvent(StaffViewModel.UserEvent.OnEmail(it), this)
        }
    }

    private fun setupBtnChangeStatus() {
        binding.layoutDisable.setOnClickListener {
            ChangeDisableDialog(this, mStatus = false) {
                status = it
                binding.edStatus.text = getStatus(it)
            }.show()
        }
    }

    private fun setData() {
        mUser = intent.getSerializableExtra("user") as? Datum
        mUser?.let {
            status = it.status
            binding.edName.setText(it.name)
            binding.edEmail.setText(it.email)
            binding.edNumber.setText(it.phoneNumber)
            binding.edRole.text = getRole(it.role)
            binding.edStatus.text = getStatus(it.status)
            Log.e("tuvm", "setupObserver: $it");
        }
    }

    private fun getRole(role: String): String {
        for (r in ROLE.values()) {
            if (r.toString() == role) {
                return r.value
            }
        }
        return role
    }

    private fun getStatus(status: Boolean): String = if (status) "Đang hoạt đông" else "Vô hiệu hóa"
//    override fun setupObserver() {
//        viewModel.listUser
//    }

}