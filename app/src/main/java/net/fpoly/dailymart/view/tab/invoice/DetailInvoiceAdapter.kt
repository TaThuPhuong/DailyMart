package net.fpoly.dailymart.view.tab.invoice

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.ProductInvoiceParam
import net.fpoly.dailymart.databinding.ItemDetailInvoiceBinding
import net.fpoly.dailymart.view.detailinvoice.DetailInvoiceActivity
import net.fpoly.dailymart.view.detailinvoice.DetailInvoiceViewModel

class DetailInvoiceAdapter(
    private val activity: DetailInvoiceActivity,
    private val viewModel: DetailInvoiceViewModel
) :
    ListAdapter<ProductInvoiceParam, DetailInvoiceAdapter.DetailInvoiceViewHolder>(
        InVoiceDetailDiffCallback()
    ) {

    class DetailInvoiceViewHolder(val binding: ItemDetailInvoiceBinding) :
        ViewHolder(binding.root) {
        fun bind(
            activity: DetailInvoiceActivity,
            item: ProductInvoiceParam,
            viewModel: DetailInvoiceViewModel
        ) {
            binding.invoice = item
            binding.viewModel = viewModel
            binding.lifecycleOwner = activity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): DetailInvoiceViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDetailInvoiceBinding.inflate(layoutInflater, parent, false)
                return DetailInvoiceViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailInvoiceViewHolder {
        return DetailInvoiceViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DetailInvoiceViewHolder, position: Int) {
        val invoiceDetail = getItem(position)
        holder.bind(activity, invoiceDetail, viewModel)
    }
}

@BindingAdapter("productDetailItems")
fun setItems(listView: RecyclerView, items: List<ProductInvoiceParam>?) {
    items?.let { params ->
        val new = params.filter { it.quantity > 0 }.toMutableList()
        (listView.adapter as DetailInvoiceAdapter).submitList(new)
        (listView.adapter as DetailInvoiceAdapter).notifyItemRangeChanged(0, new.size)
    }
}

@BindingAdapter("productDetailItemsRefund")
fun setRefundItems(listView: RecyclerView, items: List<ProductInvoiceParam>?) {
    items?.let { params ->
        val new = params.filter { it.quantity < 0 }.toMutableList()
        (listView.adapter as DetailInvoiceAdapter).submitList(new)
        (listView.adapter as DetailInvoiceAdapter).notifyItemRangeChanged(0, new.size)
    }
}

class InVoiceDetailDiffCallback : DiffUtil.ItemCallback<ProductInvoiceParam>() {
    override fun areContentsTheSame(
        oldItem: ProductInvoiceParam,
        newItem: ProductInvoiceParam
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(
        oldItem: ProductInvoiceParam,
        newItem: ProductInvoiceParam
    ): Boolean {
        return oldItem.id == newItem.id
    }

}