package net.fpoly.dailymart.view.tab.invoice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.DetailInvoice
import net.fpoly.dailymart.databinding.ItemDetailInvoiceBinding

class DetailInvoiceAdapter(private val viewModel: InvoiceViewModel) :
    ListAdapter<DetailInvoice, DetailInvoiceAdapter.DetailInvoiceViewHolder>(
        InVoiceDetailDiffCallback()
    ) {

    class DetailInvoiceViewHolder(val binding: ItemDetailInvoiceBinding) :
        ViewHolder(binding.root) {
        fun bind(item: DetailInvoice, viewModel: InvoiceViewModel) {
            binding.invoice = item
            binding.viewModel = viewModel
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
        holder.bind(invoiceDetail, viewModel)
    }
}

@BindingAdapter("invoiceDetailItems")
fun setItems(listView: RecyclerView, items: List<DetailInvoice>?) {
    items?.let {
        (listView.adapter as DetailInvoiceAdapter).submitList(items)
    }
}

class InVoiceDetailDiffCallback : DiffUtil.ItemCallback<DetailInvoice>() {
    override fun areContentsTheSame(oldItem: DetailInvoice, newItem: DetailInvoice): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: DetailInvoice, newItem: DetailInvoice): Boolean {
        return oldItem.id == newItem.id
    }
}