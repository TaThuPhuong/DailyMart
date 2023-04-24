package net.fpoly.dailymart.view.stock.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.databinding.ItemStockReportBinding
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.view.stock.StockCheck

class StockReportAdapter(private var mListItem: List<StockCheck>) :
    RecyclerView.Adapter<StockReportAdapter.ItemViewStockReport>() {

    class ItemViewStockReport(val binding: ItemStockReportBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<StockCheck>) {
        mListItem = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewStockReport {
        return ItemViewStockReport(
            ItemStockReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mListItem.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewStockReport, position: Int) {
        with(holder) {
            with(mListItem[position]) {
                binding.tvName.text = this.name
                binding.tvDate.text = "HSD: ${this.date.date2String()}"
                binding.tvBarcode.text = "Barcode: ${this.barcode}"
                binding.tvNote.text = this.note
            }
        }
    }
}