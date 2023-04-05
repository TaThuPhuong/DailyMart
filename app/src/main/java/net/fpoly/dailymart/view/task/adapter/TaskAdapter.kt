package net.fpoly.dailymart.view.task.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ItemTaskBinding
import net.fpoly.dailymart.extension.view_extention.setImage
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskAdapter(
    private var mListTask: List<Task>,
    private val onClick: (Task) -> Unit,
) :
    RecyclerView.Adapter<TaskAdapter.ItemView>() {

    class ItemView(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("SimpleDateFormat")
    private val timeFormatter = SimpleDateFormat("HH:mm")

    @SuppressLint("NotifyDataSetChanged")
    fun setTaskData(listTask: List<Task>) {
        mListTask = listTask
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mListTask.size
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListTask[position]) {
                if (this.finish) {
                    binding.root.setBackgroundResource(R.drawable.bg_green_light_bd_rd_10)
                    binding.imvTask.setImage(R.drawable.ic_finish_task)
                } else if (System.currentTimeMillis() > this.deadline) {
                    binding.root.setBackgroundResource(R.drawable.bg_red_ff44c_rd_8)
                    binding.imvTask.setImage(R.drawable.ic_task_out_of_date)
                } else {
                    binding.root.setBackgroundResource(R.drawable.bg_blue_light_bd_rd_10)
                    binding.imvTask.setImage(R.drawable.ic_task_received)
                }
                binding.tvTitle.text = this.title
                binding.tvReceiver.text = this.idReceiver.name
                binding.tvTime.text = "Từ ${timeFormatter.format(this.createAt)} -" +
                        " ${timeFormatter.format(this.deadline)}"
                binding.tvDate.text = SimpleDateFormat("dd.MM.yyyy").format(this.createAt)
                binding.root.setOnClickListener {
                    onClick(this)
                }
            }
        }
    }
}