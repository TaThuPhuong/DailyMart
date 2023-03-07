package net.fpoly.dailymart.view.task

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityAddTaskBinding
import java.text.SimpleDateFormat

class AddTaskActivity : BaseActivity<ActivityAddTaskBinding>(ActivityAddTaskBinding::inflate),
    View.OnClickListener {

    private val TAG = "YingMing"

    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat("HH : mm")

    override fun setOnClickListener() {
        binding.imvBack.setOnClickListener(this)
        binding.btnAddTask.setOnClickListener(this)
        binding.layoutTimeStart.setOnClickListener(this)
        binding.layoutTimeEnd.setOnClickListener(this)
    }

    override fun setupData() {

    }

    override fun setupObserver() {

    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v) {
            binding.imvBack -> finish()
            binding.layoutTimeStart -> {
                PickTimeDialog(this) { time ->
                    binding.tvTimeStart.text = "Bắt đầu: ${timeFormat.format(time)}"
                }.show()
            }
            binding.layoutTimeEnd -> {
                PickTimeDialog(this) { time ->
                    binding.tvTimeEnd.text = "Kết thúc: ${timeFormat.format(time)}"
                }.show()
            }
        }
    }
}