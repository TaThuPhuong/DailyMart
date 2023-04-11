package net.fpoly.dailymart.view.tab.invoice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.ProductInvoiceParam
import net.fpoly.dailymart.databinding.ItemProductInvoiceBinding
import net.fpoly.dailymart.view.pay.AddInvoiceExportViewModel

class InvoiceProductAdapter(private val viewModel: AddInvoiceExportViewModel) :
    ListAdapter<ProductInvoiceParam, InvoiceProductAdapter.ProductInvoiceViewHolder>(
        ProductInvoiceDiffCallback()
    ) {

    class ProductInvoiceViewHolder(val binding: ItemProductInvoiceBinding) :
        ViewHolder(binding.root) {
        fun bind(item: ProductInvoiceParam, viewModel: AddInvoiceExportViewModel) {
            binding.product = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ProductInvoiceViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductInvoiceBinding.inflate(layoutInflater, parent, false)
                return ProductInvoiceViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductInvoiceViewHolder.from(parent)

    override fun onBindViewHolder(holder: ProductInvoiceViewHolder, position: Int) {
        val invoiceDetail = getItem(position)
        holder.bind(invoiceDetail, viewModel)
    }
}

class ProductInvoiceDiffCallback : DiffUtil.ItemCallback<ProductInvoiceParam>() {
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