package net.fpoly.dailymart.view.supplier

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.ItemSupplierBinding

class SupplierAdapter(private val viewModel: SupplierViewModel) :
    ListAdapter<Supplier, SupplierAdapter.SupplierViewHolder>(SupplierDiffCallback()) {

    class SupplierViewHolder(val binding: ItemSupplierBinding) : ViewHolder(binding.root) {
        fun bind(viewModel: SupplierViewModel, item: Supplier) {
            binding.viewmodel = viewModel
            binding.supplier = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): SupplierViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSupplierBinding.inflate(layoutInflater, parent, false)
                return SupplierViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder =
        SupplierViewHolder.from(parent)

    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

}

class SupplierDiffCallback : DiffUtil.ItemCallback<Supplier>() {
    override fun areItemsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("supplierItems")
fun setItemsSupplier(list: RecyclerView, items: List<Supplier>?){
    items?.let {
        (list.adapter as SupplierAdapter).submitList(items)
    }
}