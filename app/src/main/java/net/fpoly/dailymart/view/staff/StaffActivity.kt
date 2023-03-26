package net.fpoly.dailymart.view.staff

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityStaffBinding
import net.fpoly.dailymart.view.add_staff.AddStaffActivity
import net.fpoly.dailymart.view.add_staff.StaffAdapter

class StaffActivity : BaseActivity<ActivityStaffBinding>(ActivityStaffBinding::inflate) {

    private val viewModel: StaffViewModel by viewModels { AppViewModelFactory }
    private lateinit var mStaffAdapter: StaffAdapter
    private var mListUser: List<User> = ArrayList()

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
        viewModel.listUser.observe(this) { users ->
            mStaffAdapter.setUserData(users)
            mListUser = users
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycleView() {
        mStaffAdapter = StaffAdapter(this, mListUser) {
//                task ->
//            OptionTaskDialog(this, task.finish,
//                onShowDetail = {},
//                onFinish = {
//                    FinishTaskConfirmDialog(this) { comment, time ->
//                        task.comment = comment ?: ""
//                        if (time != 0L) {
//                            task.finishTime = time
//                        }
//                        viewModel.onFinish(task)
//                    }.show()
//                },
//                onEdit = {
//
//                }, onDelete = {
//                    DeleteTaskConfirmDialog(this) {
//                        val snack =
//                            Snackbar.make(binding.root, "Đã xóa", Snackbar.LENGTH_LONG)
//                        snack.setAction("Hoàn tác") {
//                            viewModel.onRestore()
//                        }
//                        snack.show()
//                        viewModel.onDeleteTask(task)
//                    }.show()
//                }).show()
        }
        binding.rcvListStaff.adapter = mStaffAdapter
    }
}