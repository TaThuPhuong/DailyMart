package net.fpoly.dailymart.view.task.add_new

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityAddTaskBinding
import net.fpoly.dailymart.extention.view_extention.getTextOnChange
import net.fpoly.dailymart.extention.view_extention.gone
import net.fpoly.dailymart.extention.view_extention.visible
import net.fpoly.dailymart.view.task.PickTimeDialog
import java.text.SimpleDateFormat

class AddTaskActivity : BaseActivity<ActivityAddTaskBinding>(ActivityAddTaskBinding::inflate),
    View.OnClickListener {

    private val TAG = "YingMing"

    private val viewModel: AddTaskViewModel by viewModels { AppViewModelFactory }

    private var mListUser: List<User> = ArrayList()

    private lateinit var mStaffAdapter: StaffAdapter

    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat("HH : mm")

    override fun setOnClickListener() {
        binding.imvBack.setOnClickListener(this)
        binding.btnAddTask.setOnClickListener(this)
        binding.layoutTimeStart.setOnClickListener(this)
        binding.layoutTimeEnd.setOnClickListener(this)
    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initRecycleView()
        setOnTextChange()
    }

    override fun setupObserver() {
        viewModel.listUser.observe(this) { users ->
            Log.d(TAG, "setupObserver: $users")
            mListUser = users
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v) {
            binding.imvBack -> finish()
            binding.layoutTimeStart -> {
                PickTimeDialog(this) { time ->
                    binding.tvTimeStart.text = "Bắt đầu: ${timeFormat.format(time)}"
                    viewModel.onEvent(AddTaskEvent.TimeStartChange(time))
                }.show()
            }
            binding.layoutTimeEnd -> {
                PickTimeDialog(this) { time ->
                    binding.tvTimeEnd.text = "Kết thúc: ${timeFormat.format(time)}"
                    viewModel.onEvent(AddTaskEvent.TimeEndChange(time))
                }.show()
            }
            binding.btnAddTask -> {
                viewModel.onEvent(AddTaskEvent.AddNew)
                finish()
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
            Log.d(TAG, "setOnTextChange: $it")
            val listFilter =
                mListUser.filter { user ->
                    user.id.contains(it, true)
                            || user.name.contains(it, true)
                }
            Log.d(TAG, "setOnTextChange: $listFilter")
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