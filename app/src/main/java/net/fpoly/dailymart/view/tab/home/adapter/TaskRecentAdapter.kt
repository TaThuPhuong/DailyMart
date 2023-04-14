package net.fpoly.dailymart.view.tab.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ItemTaskRecentBinding
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.convertTimeInMillisToLastTimeString

class TaskRecentAdapter(
    val mUser: User,
    var mListTask: List<Task>,
    private val onClick: (Task) -> Unit
) :
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
                binding.tvMessage.text = getMessage(this)
                binding.tvTime.text = convertTimeInMillisToLastTimeString(this.updatedAt.time)
                binding.root.setOnClickListener {
                    onClick(this)
                }
            }
        }
    }

    private fun getMessage(task: Task): String {
        return if (task.createAt == task.updatedAt) {
            when (mUser.id) {
                task.idCreator.id -> {
                    "Bạn đã giao ${task.title} cho ${task.idReceiver.name}"
                }
                task.idReceiver.id -> {
                    "Bạn đã nhận được nhiệm vụ ${task.title}"
                }
                else -> {
                    "${task.idReceiver.name} đã nhận được nhiệm vụ ${task.title}"
                }
            }
        } else {
            when (mUser.id) {
                task.idCreator.id -> {
                    if (task.finish) {
                        "Bạn đã nhận xét về task ${task.title}"
                    } else {
                        "Bạn đã chỉnh sửa task ${task.title}"
                    }
                }
                task.idReceiver.id -> {
                    if (task.finish) {
                        "Bạn đã hoàn thành task ${task.title}"
                    } else {
                        "${task.idCreator.name} đã chỉnh sửa task ${task.title}"
                    }
                }
                else -> {
                    if (task.finish) {
                        if (task.comment.isEmpty()) {
                            "${task.idReceiver.name} đã hoàn thành ${task.title}"
                        } else {
                            "${task.idCreator.name} đã nhận xét task ${task.title}"
                        }
                    } else {
                        "${task.idCreator.name} đã chỉnh sửa ${task.title}"
                    }
                }
            }
        }
    }
}