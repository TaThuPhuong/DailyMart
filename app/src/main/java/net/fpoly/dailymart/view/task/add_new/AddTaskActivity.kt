package net.fpoly.dailymart.view.task.add_new

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.databinding.ActivityAddTaskBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.view.task.PickTimeDialog
import net.fpoly.dailymart.view.task.adapter.StaffAdapter
import java.text.SimpleDateFormat

class AddTaskActivity : BaseActivity<ActivityAddTaskBinding>(ActivityAddTaskBinding::inflate),
    View.OnClickListener {

    private val TAG = "YingMing"

    private val viewModel: AddTaskViewModel by viewModels { AppViewModelFactory }

    private var mListUser: List<UserRes> = ArrayList()

    private lateinit var mStaffAdapter: StaffAdapter

    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat("HH : mm")

    private lateinit var mLoadingDialog: LoadingDialog

    override fun setOnClickListener() {
        binding.imvBack.setOnClickListener(this)
        binding.btnAddTask.setOnClickListener(this)
        binding.layoutTimeEnd.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun setupData() {
        mLoadingDialog = LoadingDialog(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.tvTimeStart.text = "Bắt đầu: ${timeFormat.format(System.currentTimeMillis())}"
        viewModel.getAllUser()
        initRecycleView()
        setOnTextChange()
    }

    override fun setupObserver() {
        viewModel.listUser.observe(this) { users ->
            if (users != null) {
                mListUser = users
                mStaffAdapter.setUserData(users)
            }
        }
        viewModel.addSuccess.observe(this) {
            if (it) {
                mLoadingDialog.hideLoading()
                finish()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v) {
            binding.imvBack -> finish()
            binding.layoutTimeEnd -> {
                PickTimeDialog(this) { time ->
                    binding.tvTimeEnd.text = "Kết thúc: ${timeFormat.format(time)}"
                    viewModel.onEvent(AddTaskEvent.TimeEndChange(time))
                }.show()
            }
            binding.btnAddTask -> {
                mLoadingDialog.showLoading()
                viewModel.onEvent(AddTaskEvent.AddNew)
            }
        }
    }

    private fun initRecycleView() {
        mStaffAdapter = StaffAdapter(mListUser) { user ->
            viewModel.onEvent(AddTaskEvent.ReceiverChange(user))
            binding.edName.setText(user.name)
            binding.rcvListUser.gone()
        }
        binding.rcvListUser.adapter = mStaffAdapter
    }

    private fun setOnTextChange() {

        binding.edTitle.getTextOnChange {
            viewModel.onEvent(AddTaskEvent.TitleChange(it))
        }
        binding.edName.getTextOnChange {
            val listFilter =
                mListUser.filter { user ->
                    user.id.contains(it, true)
                            || user.name.contains(it, true)
                }
            if (listFilter.isNotEmpty()) {
                mStaffAdapter.setUserData(listFilter)
                binding.rcvListUser.visible()
            } else {
                mStaffAdapter.setUserData(ArrayList())
                binding.rcvListUser.gone()
            }
        }
        binding.edDescription.getTextOnChange {
            viewModel.onEvent(AddTaskEvent.DescriptionChange(it))
        }
    }
}