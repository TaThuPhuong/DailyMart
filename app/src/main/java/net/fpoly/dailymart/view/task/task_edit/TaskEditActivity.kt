package net.fpoly.dailymart.view.task.task_edit

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.databinding.ActivityTaskEditBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.view.task.PickTimeDialog
import net.fpoly.dailymart.view.task.add_new.AddTaskEvent
import java.text.SimpleDateFormat

class TaskEditActivity :
    BaseActivity<ActivityTaskEditBinding>(ActivityTaskEditBinding::inflate) {

    private val TAG = "YingMing"
    private val viewModel by viewModels<TaskEditViewModel> { AppViewModelFactory }

    private var mTask: Task? = null

    private var mLoadingDialog: LoadingDialog? = null

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun setOnClickListener() {
        binding.tvTimeEnd.setOnClickListener {
            PickTimeDialog(this) { time ->
                binding.tvTimeEnd.text = "Kết thúc: ${SimpleDateFormat("HH:mm").format(time)}"
                viewModel.onEvent(EditEvent.OnChangeEndTime(time))
            }.show()
        }
        binding.btnSave.setOnClickListener {
            mLoadingDialog!!.showLoading()
            viewModel.onEvent(EditEvent.OnSave)
        }
        binding.imvBack.setOnClickListener { finish() }
        binding.scFinish.setOnCheckedChangeListener { _, b ->
            viewModel.onEvent(EditEvent.OnFinishChange(b))
        }
    }

    override fun setupData() {
        mLoadingDialog = LoadingDialog(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mTask = intent.getSerializableExtra(Constant.TASK) as Task
        mTask?.let {
            viewModel.setTask(it)
        }
    }

    override fun setupObserver() {
        viewModel.message.observe(this) {
            if (it.isNotBlank()) {
                showToast(this, it)
            }
        }
        viewModel.updateTaskSuccess.observe(this) {
            if (it != null) {
                mLoadingDialog?.hideLoading()
                if (it) finish()
            }
        }
    }
}