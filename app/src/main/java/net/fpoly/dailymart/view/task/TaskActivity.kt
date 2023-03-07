package net.fpoly.dailymart.view.task

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityTaskBinding
import net.fpoly.dailymart.extention.view_extention.setMarginsStatusBar

class TaskActivity : BaseActivity<ActivityTaskBinding>(ActivityTaskBinding::inflate),
    View.OnClickListener {

    private val viewModel: TaskViewModel by viewModels { AppViewModelFactory }

    override fun setOnClickListener() {
        binding.imvAdd.setOnClickListener(this)
        binding.layoutAssigned.setOnClickListener(this)
        binding.layoutFinish.setOnClickListener(this)
    }

    override fun setupData() {
        binding.layoutToolbar.setMarginsStatusBar(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupObserver() {

    }


    override fun onClick(v: View?) {
        when (v) {
            binding.imvAdd -> {
                startActivity(Intent(this, AddTaskActivity::class.java))
//                OptionTaskDialog(this,
//                    onEdit = {},
//                    onDelete = {
//                        DeleteTaskConfirmDialog(this) {
//
//                        }.show()
//                    }
//                ).show()
            }
        }
    }
}