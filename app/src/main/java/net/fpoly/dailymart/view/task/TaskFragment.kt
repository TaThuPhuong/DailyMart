package net.fpoly.dailymart.view.task

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.databinding.FragmentTaskBinding
import net.fpoly.dailymart.view.task.adapter.TaskAdapter

class TaskFragment : BaseFragment<FragmentTaskBinding>(FragmentTaskBinding::inflate) {

    private val TAG = "YingMing"

    private val viewModel: TaskViewModel by activityViewModels()

    private lateinit var mTaskAdapter: TaskAdapter

    private var mListTask: List<Task> = ArrayList()

    override fun setupData() {
        initRecycleView()
    }

    override fun setupObserver() {
        viewModel.listTask.observe(this) {
            Log.d(TAG, "list task: $it")
            mTaskAdapter.setTaskData(it)
            mListTask = it
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycleView() {
        mTaskAdapter = TaskAdapter(mContext, mListTask) { task ->
            OptionTaskDialog(mContext, task.finish,
                onShowDetail = {},
                onFinish = {
                    FinishTaskConfirmDialog(mContext) { comment, time ->
                        task.comment = comment ?: ""
                        if (time != 0L) {
                            task.finishTime = time
                        }
                        viewModel.onFinish(task)
                    }.show()
                },
                onEdit = {

                }, onDelete = {
                    DeleteTaskConfirmDialog(mContext) {
                        val snack =
                            Snackbar.make(binding.root, "Đã xóa", Snackbar.LENGTH_LONG)
                        snack.setAction("Hoàn tác") {
                            viewModel.onRestore()
                        }
                        snack.show()
                        viewModel.onDeleteTask(task)
                    }.show()
                }).show()
        }
        binding.rcvListTask.adapter = mTaskAdapter
    }

    companion object {
        fun newInstance() = TaskFragment()
    }
}
