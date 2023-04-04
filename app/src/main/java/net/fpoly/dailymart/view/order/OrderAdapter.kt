package net.fpoly.dailymart.view.order

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.database.InvoiceDao
import net.fpoly.dailymart.data.database.OrderInfo
import net.fpoly.dailymart.data.model.OrderResponse
import net.fpoly.dailymart.data.model.param.OrderParam
import net.fpoly.dailymart.databinding.ItemOrderBinding

class OrderAdapter(
    val mContext: Context,
    var mListOrder: List<OrderResponse> = ArrayList()
) :
    RecyclerView.Adapter<OrderAdapter.ItemView>() {
    class ItemView(val binding: ItemOrderBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<OrderResponse>) {
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
//                binding.tvExpiryDate.text = this.invoiceDetails[0].product
                binding.tvProductName.text = this.invoiceDetails[0].product.productName
                binding.tvQuantity.text = "SL: ${this.invoiceDetails[0].quantity}"
                binding.tvTotalInvoice.text = "Đơn giá: ${this.invoiceDetails[0].price}"
            }
        }
    }

    override fun getItemCount(): Int {
        return mListOrder.size
    }
}
