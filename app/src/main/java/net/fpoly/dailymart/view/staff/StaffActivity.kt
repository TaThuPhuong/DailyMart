package net.fpoly.dailymart.view.staff

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.databinding.ActivityStaffBinding
import net.fpoly.dailymart.view.add_staff.AddStaffActivity
import net.fpoly.dailymart.view.staff.details.DetailsStaffActivity

class StaffActivity : BaseActivity<ActivityStaffBinding>(ActivityStaffBinding::inflate) {

    private val viewModel: StaffViewModel by viewModels { AppViewModelFactory }
    private lateinit var mStaffAdapter: StaffAdapter
    private var mListUser: List<Datum> = ArrayList()

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.imvBack.setOnClickListener { finish() }
        binding.tvAddNew.setOnClickListener {
            startActivity(Intent(this, AddStaffActivity::class.java))
        }
        initRecycleView()
    }

    override fun setupObserver() {
//        viewModel.listUser.observe(this) { users ->
//            mStaffAdapter.setUserData(users)
//            mListUser = users
//        }
        viewModel.user.observe(this) { user ->
            mStaffAdapter.setUserData(user)
            mListUser = user
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycleView() {
        mStaffAdapter = StaffAdapter(this, mListUser) { user ->
            val intent = Intent(this, DetailsStaffActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
        binding.rcvListStaff.adapter = mStaffAdapter
    }
}