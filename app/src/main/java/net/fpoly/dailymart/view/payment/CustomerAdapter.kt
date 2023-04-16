package net.fpoly.dailymart.view.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.data.model.Customer
import net.fpoly.dailymart.databinding.ItemCustomerPickBinding

class CustomerAdapter(private val viewModel: PaymentViewModel, private val callback: (Customer) -> Unit) : ListAdapter<Customer, CustomerAdapter.CustomerViewHolder>(CustomerDiffCallback()) {

    class CustomerViewHolder(val binding: ItemCustomerPickBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(customer: Customer, viewModel: PaymentViewModel, callback: (Customer) -> Unit){
            binding.viewmodel = viewModel
            binding.customer = customer
            binding.root.setOnClickListener {
                callback(customer)
            }
        }

        companion object {
            fun from(parent: ViewGroup): CustomerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCustomerPickBinding.inflate(layoutInflater, parent, false)
                return CustomerViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CustomerViewHolder.from(parent)

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = getItem(position)
        holder.bind(customer, viewModel, callback)
    }
}

class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
    override fun areItemsTheSame(oldItem: Customer, newItem: Customer) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Customer, newItem: Customer) = oldItem.id == newItem.id
}