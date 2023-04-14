package net.fpoly.dailymart.view.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.ProductInvoiceParam
import net.fpoly.dailymart.databinding.ItemPaymentProductBinding

class PaymentAdapter :
    ListAdapter<ProductInvoiceParam, PaymentAdapter.PaymentViewHolder>(ProductInvoiceDiffCallback()) {

    class PaymentViewHolder(val binding: ItemPaymentProductBinding) : ViewHolder(binding.root) {
        fun onBind(product: ProductInvoiceParam) {
            binding.product = product
        }

        companion object {
            fun from(parent: ViewGroup): PaymentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPaymentProductBinding.inflate(layoutInflater, parent, false)
                return PaymentViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder =
        PaymentViewHolder.from(parent)

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val product = getItem(position)
        holder.onBind(product)
    }

}

class ProductInvoiceDiffCallback : DiffUtil.ItemCallback<ProductInvoiceParam>() {
    override fun areItemsTheSame(
        oldItem: ProductInvoiceParam,
        newItem: ProductInvoiceParam
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: ProductInvoiceParam,
        newItem: ProductInvoiceParam
    ) = oldItem.id == newItem.id
}