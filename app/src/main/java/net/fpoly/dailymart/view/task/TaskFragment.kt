package net.fpoly.dailymart.view.task

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.databinding.FragmentTaskBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.task.adapter.TaskAdapter
import net.fpoly.dailymart.view.task.task_detail.TaskDetailActivity
import net.fpoly.dailymart.view.task.task_edit.TaskEditActivity


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
            mTaskAdapter.setTaskData(it)
            mListTask = it
        }
        viewModel.message.observe(this) {
            if (it != null) showToast(mContext, it)
        }
        viewModel.textSearch.observe(this) {
            if (it == null) mTaskAdapter.setTaskData(mListTask)
            if (it != null) onSearch(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycleView() {
        val user = SharedPref.getUser(mContext)
        mTaskAdapter = TaskAdapter(mListTask) { task ->
            if (user.role == ROLE.manager || user.id == task.idReceiver.id) {
                OptionTaskDialog(mContext, task.finish, user.id == task.idReceiver.id,
                    onShowDetail = {
                        Intent(mContext, TaskDetailActivity::class.java).also {
                            it.putExtra(Constant.TASK, task)
                            startActivity(it)
                        }
                    },
                    onFinish = {
                        FinishTaskConfirmDialog(mContext = mContext) { comment, time ->
                            task.comment = comment ?: ""
                            task.finish = true
                            if (task.finishTime == 0L) {
                                task.finishTime = time
                            }
                            viewModel.onFinish(task)
                        }.show()
                    },
                    onEdit = {
                        Intent(mContext, TaskEditActivity::class.java).also {
                            it.putExtra(Constant.TASK, task)
                            startActivity(it)
                        }
                    }, onDelete = {
//                        DeleteTaskConfirmDialog(mContext) {
//                            viewModel.onDeleteTask(task) {
//                                val snack =
//                                    Snackbar.make(binding.root, "Đã xóa", Snackbar.LENGTH_LONG)
//                                snack.setAction("Hoàn tác") {
//                                    viewModel.onRestore()
//                                }
//                                snack.show()
//                            }
//                        }.show()
                    }).show()
            } else {
                Intent(mContext, TaskDetailActivity::class.java).also {
                    it.putExtra(Constant.TASK, task)
                    startActivity(it)
                }
            }
        }
        binding.rcvListTask.adapter = mTaskAdapter
    }

    private fun onSearch(s: String) {
        val listFilter =
            mListTask.filter { task ->
                task.idReceiver.name.contains(s, true) || task.title.contains(s, true)
            }
        if (listFilter.isNotEmpty()) {
            mTaskAdapter.setTaskData(listFilter)
            binding.rcvListTask.visible()
        } else {
            mTaskAdapter.setTaskData(ArrayList())
            binding.rcvListTask.gone()
        }
    }

    companion object {
        fun newInstance() = TaskFragment()
    }
}
