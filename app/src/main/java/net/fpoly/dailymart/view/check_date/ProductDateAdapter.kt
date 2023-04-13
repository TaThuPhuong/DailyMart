package net.fpoly.dailymart.view.check_date

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.ExpiryCheck
import net.fpoly.dailymart.databinding.ItemProductDateBinding
import java.text.SimpleDateFormat

class ProductDateAdapter(var mListExpiry: List<ExpiryCheck>, val onClick: (ExpiryCheck) -> Unit) :
    RecyclerView.Adapter<ProductDateAdapter.ItemView>() {
    class ItemView(val binding: ItemProductDateBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<ExpiryCheck>) {
        mListExpiry = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemProductDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mListExpiry.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListExpiry[position]) {
                binding.name.text = this.productName
                binding.date.text = SimpleDateFormat("dd/MM/yyyy").format(this.expiryDate)
            }
        }
    }
}