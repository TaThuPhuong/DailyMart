package net.fpoly.dailymart.view.tab.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.databinding.ItemTaskRecentBinding
import net.fpoly.dailymart.utils.convertTimeInMillisToLastTimeString

class TaskRecentAdapter(var mListTask: List<Task>, private val onClick: () -> Unit) :
    RecyclerView.Adapter<TaskRecentAdapter.ItemView>() {

    class ItemView(val binding: ItemTaskRecentBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Task>) {
        mListTask = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemTaskRecentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mListTask.size
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListTask[position]) {
                binding.tvMessage.text = this.title
                binding.tvTime.text = convertTimeInMillisToLastTimeString(this.updatedAt.time)
            }
        }
    }
}