package net.fpoly.dailymart.view.report

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.BestSeller
import net.fpoly.dailymart.databinding.ItemBestSellerBinding

class BestSellerAdapter(private var mListBestSeller: List<BestSeller>) :
    RecyclerView.Adapter<BestSellerAdapter.ItemView>() {
    class ItemView(val binding: ItemBestSellerBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<BestSeller>) {
        mListBestSeller = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemBestSellerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mListBestSeller.size
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListBestSeller[position]) {
                binding.tvName.text = this.sanPham.productName
                binding.tvQuantity.text = this.soLuongDaBan.toString()
            }
        }
    }
}