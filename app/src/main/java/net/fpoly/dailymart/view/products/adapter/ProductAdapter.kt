package net.fpoly.dailymart.view.products.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ItemProductBinding
import net.fpoly.dailymart.utils.toMoney

class ProductAdapter(
    val mContext: Context,
    var mListProduct: List<Product> = ArrayList(),
    val onClick: (Product) -> Unit
) :
    RecyclerView.Adapter<ProductAdapter.ItemView>() {

    class ItemView(val binding: ItemProductBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Product>) {
        mListProduct = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mListProduct.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListProduct[position]) {
                binding.tvName.text = this.name
                binding.tvId.text = "Barcode: ${this.barcode}"
                binding.tvPrice.text = "Giá bán:\n${this.sellPrice.toMoney()}"
                Glide.with(mContext).load(this.img_product)
                    .placeholder(R.drawable.img_default)
                    .into(binding.imvImage)
                binding.root.setOnClickListener {
                    onClick(this)
                }
            }
        }
    }
}