package net.fpoly.dailymart.view.profile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityProfileBinding
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.hide
import net.fpoly.dailymart.firbase.storege.Images
import net.fpoly.dailymart.utils.ImagesUtils
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.staff.details.UpdateEvent

class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate),
    View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModels { AppViewModelFactory }

    private var mUser: User? = null

    private var onChangeAvatar = false
    private var mLoadingDialog: LoadingDialog? = null

    override fun setOnClickListener() {
        binding.btnBack.setOnClickListener(this)
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
                mLoadingDialog?.hideLoading()
            }
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.btnBack -> finish()
            binding.imvAvatar -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkPermissionImage()
                } else {
                    ImagesUtils.checkPermissionPickImage(this, binding.imvAvatar) {
                        onChangeAvatar = true
                    }
                }
            }
            binding.tvSave -> {
                mLoadingDialog?.showLoading()
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
            if (it.trim().isEmpty()) {
                binding.tvNameError.text = it.blankException()
            } else if (it.length < 3) {
                binding.tvNameError.text = "Tên từ 4 kí tự"
            } else {
                binding.tvNameError.text = ""
            }
        }
        binding.edPhone.getTextOnChange {
            mUser = mUser?.copy(phone = it)
            if (it.trim().isEmpty()) {
                binding.tvPhoneError.text = it.blankException()
            } else if (!isPhoneNumberValid(it)) {
                binding.tvPhoneError.text = "Số điện thoại không hợp lệ!"
            } else {
                binding.tvPhoneError.text = ""
            }
        }
        binding.edEmail.getTextOnChange {
            mUser = mUser?.copy(email = it)
            if (it.trim().isEmpty()) {
                binding.tvEmailError.text = it.blankException()
            } else if (!isEmailValid(it)) {
                binding.tvEmailError.text = "Email không hợp lệ!"
            } else {
                binding.tvEmailError.text = ""
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionImage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                2
            )
        } else {
            ImagesUtils.openImagesPicker(this, binding.imvAvatar) {
                onChangeAvatar = true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImagesUtils.openImagesPicker(this, binding.imvAvatar) {
                    onChangeAvatar = true
                }
            } else {
                Toast.makeText(this, "Vui lòng câps quyền", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getDisable(status: Boolean): String {
        return if (!status) {
            "Vô hiệu hóa"
        } else {
            "Đang hoạt động"
        }
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val regex =
            Regex("^\\+?(0)([3|5|7|8|9]\\d{8})$")
        return regex.matches(phoneNumber)
    }

    private fun isEmailValid(email: String): Boolean {
        val regex = Regex("^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,}\$")
        return regex.matches(email)
    }
}