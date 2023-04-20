package net.fpoly.dailymart.view.task.task_detail

import android.os.Build
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.databinding.ActivityTaskDetailBinding
import net.fpoly.dailymart.utils.Constant

class TaskDetailActivity :
    BaseActivity<ActivityTaskDetailBinding>(ActivityTaskDetailBinding::inflate) {

    private val viewModel by viewModels<TaskDetailViewModel> { AppViewModelFactory }

    private var mTask: Task? = null
    private var mTaskId: String? = null

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mTask = intent.getSerializableExtra(Constant.TASK) as Task?
        mTaskId = intent.getStringExtra(Constant.TASK_ID)
        mTaskId?.let {
            viewModel.getTaskById(it)
        }
        mTask?.let {
            viewModel.setTask(it)
        }
        binding.btnBack.setOnClickListener { finish() }
        binding.btnClose.setOnClickListener { finish() }
    }

    override fun setupObserver() {

    }
}