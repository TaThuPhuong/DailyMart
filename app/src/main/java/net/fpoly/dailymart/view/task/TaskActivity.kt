package net.fpoly.dailymart.view.task

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityTaskBinding
import net.fpoly.dailymart.extention.view_extention.*
import net.fpoly.dailymart.view.task.add_new.AddTaskActivity

class TaskActivity : BaseActivity<ActivityTaskBinding>(ActivityTaskBinding::inflate),
    View.OnClickListener {

    private val TAG = "YingMing"

    private val viewModel: TaskViewModel by viewModels { AppViewModelFactory }

    private lateinit var mTaskAdapter: TaskAdapter

    private var mListUser: List<User> = ArrayList()

    private var mListTask: List<Task> = ArrayList()

    private var isAssignedOpen = true

    override fun setOnClickListener() {
        binding.imvBack.setOnClickListener(this)
        binding.imvAdd.setOnClickListener(this)
        binding.imvClear.setOnClickListener(this)
    }

    override fun setupData() {
        binding.layoutToolbar.setMarginsStatusBar(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initRecycleView()
        setSearchTask()
        setSwipe()
    }

    override fun setupObserver() {
        viewModel.listTask.observe(this) { tasks ->
            mTaskAdapter.setTaskData(tasks)
            mListTask = tasks
            if (tasks.isNotEmpty()) {
                binding.tvNoData.gone()
            } else {
                binding.tvNoData.visible()
            }
        }
        viewModel.listUser.observe(this) { users ->
            mTaskAdapter.setUserData(users)
            mListUser = users
        }
        viewModel.tabAssignedOpen.observe(this) {
            isAssignedOpen = it
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.imvBack -> finish()
            binding.imvAdd -> {
                startActivity(Intent(this, AddTaskActivity::class.java))
            }
            binding.imvClear -> {
                binding.edSearch.setText("")
                mTaskAdapter.setTaskData(mListTask)
                binding.imvClear.gone()
                binding.tvNoData.gone()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onOpenTab(1)
    }

    private fun setSearchTask() {
        binding.edSearch.getTextOnChange {
            if (it.isBlank()) {
                mTaskAdapter.setTaskData(mListTask)
                binding.imvClear.gone()
                binding.tvNoData.gone()
            } else {
                val listFilerTitle = mListTask.filter { task ->
                    task.title.contains(it, true)
                }
                binding.imvClear.visible()
                val listFiler: ArrayList<Task> = ArrayList()
                listFiler.addAll(listFilerTitle)
                listFiler.addAll(getListTaskByName(it))
                mTaskAdapter.setTaskData(listFiler.distinct())
                if (listFiler.isNotEmpty()) {
                    binding.tvNoData.gone()
                } else {
                    binding.tvNoData.visible()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setSwipe() {
        Log.d(TAG, "setSwipe: ")

        binding.layoutContent.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                Log.d(TAG, "onSwipeLeft: ")
                if (isAssignedOpen) {
                    viewModel.onOpenTab(2)
                } else {
                    viewModel.onOpenTab(1)
                }
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                Log.d(TAG, "onSwipeRight: ")
                if (isAssignedOpen) {
                    viewModel.onOpenTab(2)
                } else {
                    viewModel.onOpenTab(1)
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycleView() {
        mTaskAdapter = TaskAdapter(this, mListTask, mListUser) { task ->
            OptionTaskDialog(this,
                onEdit = {

                }, onDelete = {
                    DeleteTaskConfirmDialog(this) {
                        viewModel.onDeleteTask(task)
                        mTaskAdapter.notifyDataSetChanged()
                    }.show()
                }).show()
        }
        binding.rcvListTask.adapter = mTaskAdapter
    }

    private fun getListTaskByName(name: String): List<Task> {
        val listTask = ArrayList<Task>()
        val listUser = mListUser.filter { it.name.contains(name, true) }
        for (user in listUser) {
            for (task in mListTask) {
                if (user.id.contains(task.idReceiver, true))
                    listTask.add(task)
            }
        }
        Log.d(TAG, "listTask: $listTask")
        return listTask
    }
}