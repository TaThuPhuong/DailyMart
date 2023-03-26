package net.fpoly.dailymart.view.tab.receipt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.databinding.ItemInvoiceBinding

class InvoiceAdapter(private val viewModel: ReceiptViewModel) : ListAdapter<Invoice, InvoiceAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val invoice = getItem(position)
        holder.bind(invoice, viewModel)
    }


    class ViewHolder private constructor(val binding: ItemInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind( item: Invoice, viewModel: ReceiptViewModel) {
            binding.invoice = item
            binding.viewmodel = viewModel
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemInvoiceBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Invoice>?) {
    items?.let {
        (listView.adapter as InvoiceAdapter).submitList(items)
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Invoice>() {
    override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
        return oldItem.id == newItem.id
    }
}