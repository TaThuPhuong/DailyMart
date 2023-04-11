package net.fpoly.dailymart.view.tab.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.RecentNotification
import net.fpoly.dailymart.databinding.ItemNotificationBinding
import net.fpoly.dailymart.utils.convertTimeInMillisToLastTimeString

class NotificationAdapter(
    var mListNotification: List<RecentNotification>,
    val onClick: (String) -> Unit
) :
    RecyclerView.Adapter<NotificationAdapter.ItemView>() {
    class ItemView(val binding: ItemNotificationBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<RecentNotification>) {
        mListNotification = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mListNotification.size
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListNotification[position]) {
                binding.time.text = convertTimeInMillisToLastTimeString(this.time)
                binding.title.text = this.title
                binding.message.text = this.message
                binding.root.setOnClickListener { onClick(this.value) }
            }
        }
    }
}