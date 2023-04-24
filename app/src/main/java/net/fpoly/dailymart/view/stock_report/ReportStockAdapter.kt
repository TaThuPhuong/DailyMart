package net.fpoly.dailymart.view.stock_report

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.ReportCheckStock
import net.fpoly.dailymart.databinding.ItemStaffDialogBinding
import net.fpoly.dailymart.databinding.ItemStringBinding
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.view.stock_report.ReportStockAdapter.*

class ReportStockAdapter(
    private var mList: List<ReportCheckStock>,
    private val onChose: (ReportCheckStock) -> Unit,
) :
    RecyclerView.Adapter<ItemView>() {

    class ItemView(val binding: ItemStringBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<ReportCheckStock>) {
        mList = list
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mList[position]) {
                binding.tvName.text = this.time.date2String("EEE, dd/MM/yyyy")
                binding.root.setOnClickListener {
                    onChose(this)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemStringBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}