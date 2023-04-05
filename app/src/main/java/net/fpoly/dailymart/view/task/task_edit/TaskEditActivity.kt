package net.fpoly.dailymart.view.task.task_edit

import android.util.Log
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.databinding.ActivityTaskEditBinding
import net.fpoly.dailymart.utils.Constant

class TaskEditActivity :
    BaseActivity<ActivityTaskEditBinding>(ActivityTaskEditBinding::inflate) {

    private val TAG = "YingMing"
    private val viewModel by viewModels<TaskEditViewModel> { AppViewModelFactory }

    private var mTask: Task? = null

    override fun setOnClickListener() {
        binding.layoutTimeEnd.setOnClickListener {

        }
        binding.btnSave.setOnClickListener {

        }
        binding.imvBack.setOnClickListener { finish() }
        binding.scFinish.setOnCheckedChangeListener { _, b ->
            Log.d(TAG, "setOnCheckedChangeListener: $b")
            viewModel.onEvent(EditEvent.OnFinishChange(b))
        }
    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mTask = intent.getSerializableExtra(Constant.TASK) as Task
        mTask?.let {
            viewModel.setTask(it)
        }
    }

    override fun setupObserver() {

    }
}