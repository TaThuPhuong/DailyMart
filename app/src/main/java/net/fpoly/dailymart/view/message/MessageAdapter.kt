package net.fpoly.dailymart.view.message

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.model.Message
import net.fpoly.dailymart.databinding.ItemMessageReceiveBinding
import net.fpoly.dailymart.databinding.ItemMessageSendBinding
import net.fpoly.dailymart.utils.convertTimeInMillisToLastTimeString

class MessageAdapter(
    private val mContext: Context,
    private var mListMessage: List<Message>,
    private val userId: String,
) :
    RecyclerView.Adapter<MessageAdapter.ItemView>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Message>) {
        mListMessage = list
        notifyDataSetChanged()
    }

    class ItemView(val binding: ViewBinding) : ViewHolder(binding.root) {
        fun bind(context: Context, message: Message) {
            when (binding) {
                is ItemMessageSendBinding -> {
                    binding.tvMessage.text = message.message
                    binding.tvName.text = message.name
                    binding.tvTime.text = convertTimeInMillisToLastTimeString(message.time)
                    Glide.with(context).load(message.avatar).error(R.drawable.img_avatar_default)
                        .into(binding.imvAvatar)
                }
                is ItemMessageReceiveBinding -> {
                    binding.tvMessage.text = message.message
                    binding.tvName.text = message.name
                    binding.tvTime.text = convertTimeInMillisToLastTimeString(message.time)
                    Glide.with(context).load(message.avatar).error(R.drawable.img_avatar_default)
                        .into(binding.imvAvatar)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            TYPE_SEND -> ItemMessageSendBinding.inflate(inflater, parent, false)
            TYPE_RECEIVE -> ItemMessageReceiveBinding.inflate(inflater, parent, false)
            else -> throw IllegalArgumentException("Invalid view type")
        }
        return ItemView(binding)
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(mListMessage[position]) {
            holder.bind(mContext, this)
        }
    }

    override fun getItemCount(): Int {
        return mListMessage.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mListMessage[position].idSend == userId) {
            TYPE_SEND
        } else {
            TYPE_RECEIVE
        }
    }

    companion object {
        const val TYPE_SEND = 0
        const val TYPE_RECEIVE = 1
    }
}