package net.fpoly.dailymart.view.staff

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.databinding.ActivityStaffBinding
import net.fpoly.dailymart.extension.view_extention.*
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.add_staff.AddStaffActivity
import net.fpoly.dailymart.view.staff.details.DetailsStaffActivity

class StaffActivity : BaseActivity<ActivityStaffBinding>(ActivityStaffBinding::inflate) {

    private val TAG = "tuvm"
    private val viewModel: StaffViewModel by viewModels { AppViewModelFactory }
    private lateinit var mStaffAdapter: StaffAdapter
    private var mListUser: List<Datum> = ArrayList()
    private lateinit var mUser: User

    private var mLoadingDialog: LoadingDialog? = null
    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mLoadingDialog = LoadingDialog(this)
        mUser = SharedPref.getUser(this)
        binding.imvBack.setOnClickListener { finish() }
        binding.tvAddNew.setOnClickListener {
            startActivity(Intent(this, AddStaffActivity::class.java))
        }
        binding.tvAddNew.setVisibility(mUser.role != ROLE.staff)
        binding.imvClear.setOnClickListener {
            binding.imvClear.gone()
            binding.edSearch.setText("")
            mStaffAdapter.setUserData(mListUser)
        }
        setUserSearch()
        mLoadingDialog?.showLoading()
        initRecycleView()
        viewModel.getUser()
    }

    override fun setupObserver() {
        viewModel.mListUser.observe(this) { user ->
            mStaffAdapter.setUserData(user)
            mLoadingDialog?.hideLoading()
            mListUser = user
            if (user.isNotEmpty()) {
                binding.tvNoData.gone()
            } else {
                binding.tvNoData.visible()
            }
        }
        viewModel.getUserSuccess.observe(this) {
            mLoadingDialog?.hideLoading()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycleView() {
        mStaffAdapter = StaffAdapter(this, mListUser) { user ->
            if (mUser.role == ROLE.staff) return@StaffAdapter
            val intent = Intent(this, DetailsStaffActivity::class.java)
            intent.putExtra("user", user)
            binding.edSearch.setText("")
            startActivity(intent)
        }
        binding.rcvListStaff.adapter = mStaffAdapter
    }

    private fun setUserSearch() {
        binding.edSearch.getTextOnChange {
            if (it.isBlank()) {
                mStaffAdapter.setUserData(mListUser)
                binding.imvClear.gone()
                binding.tvNoData.gone()
            } else {
                val listFilerTitle = mListUser.filter { user ->
                    user.name.contains(it, true) ||
                            user.phoneNumber.contains(it, true)
                }
                binding.imvClear.visible()
                val listFiler: ArrayList<Datum> = ArrayList()
                listFiler.addAll(listFilerTitle)
                mStaffAdapter.setUserData(listFiler)
                if (listFiler.isNotEmpty()) {
                    binding.tvNoData.gone()
                } else {
                    binding.tvNoData.visible()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mLoadingDialog?.showLoading()
        viewModel.getUser()
    }
}