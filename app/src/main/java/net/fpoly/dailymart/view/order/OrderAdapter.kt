package net.fpoly.dailymart.view.order

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.database.InvoiceDao
import net.fpoly.dailymart.data.database.OrderInfo
import net.fpoly.dailymart.data.model.param.OrderParam
import net.fpoly.dailymart.databinding.ItemOrderBinding

class OrderAdapter(
    val mContext: Context,
    var mListOrder: List<OrderParam> = ArrayList()
) :
    RecyclerView.Adapter<OrderAdapter.ItemView>() {
    class ItemView(val binding: ItemOrderBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<OrderParam>) {
        mListOrder = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListOrder[position]) {
//                binding.tvExpiryDate.text = this.dateCreate
//                binding.tvProductName.text = this.name
//                binding.tvQuantity.text = "SL: ${this.quantity}"
//                binding.tvTotalInvoice.text = "Đơn giá: ${this.total}"
            }
        }
    }

    override fun getItemCount(): Int {
        return mListOrder.size
    }
}
