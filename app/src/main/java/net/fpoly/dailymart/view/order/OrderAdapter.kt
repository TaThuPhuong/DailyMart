package net.fpoly.dailymart.view.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.ProductInvoiceParam
import net.fpoly.dailymart.databinding.ItemOrderBinding

class OrderAdapter(private val activity: OrderActivity, private val viewModel: OrderViewModel) :
    ListAdapter<ProductInvoiceParam, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {
    class OrderViewHolder(val binding: ItemOrderBinding) : ViewHolder(binding.root) {
        fun bind(product: ProductInvoiceParam, position: Int, viewModel: OrderViewModel, activity: OrderActivity) {
            binding.product = product
            binding.viewmodel = viewModel
            binding.position = position
            binding.lifecycleOwner = activity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): OrderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemOrderBinding.inflate(layoutInflater, parent, false)
                return OrderViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderViewHolder.from(parent)

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, position, viewModel, activity)
    }

}


class OrderDiffCallback : DiffUtil.ItemCallback<ProductInvoiceParam>() {
    override fun areItemsTheSame(
        oldItem: ProductInvoiceParam,
        newItem: ProductInvoiceParam
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ProductInvoiceParam,
        newItem: ProductInvoiceParam
    ) = oldItem == newItem
}
