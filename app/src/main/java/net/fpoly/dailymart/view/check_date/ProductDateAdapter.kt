package net.fpoly.dailymart.view.check_date

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.model.ExpiryCheck
import net.fpoly.dailymart.databinding.ItemProductDateBinding
import java.text.SimpleDateFormat

class ProductDateAdapter(
    private val mContext: Context,
    var mListExpiry: List<ExpiryCheck>,
    val onClick: (ExpiryCheck) -> Unit,
) :
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

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListExpiry[position]) {
                val isOutOfDate = System.currentTimeMillis() > this.expiryDate
                if (isOutOfDate) binding.date.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.red_FF444C
                    )
                )
                binding.name.text = this.productName
                binding.tvBarcode.text = "Barcode: ${this.barcode}"
                binding.date.text = SimpleDateFormat("dd/MM/yyyy").format(this.expiryDate)
                binding.root.setOnClickListener {
                    onClick(this)
                }
            }
        }
    }
}