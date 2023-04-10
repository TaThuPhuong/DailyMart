package net.fpoly.dailymart.view.work_sheet.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.databinding.ItemSheetEditBinding

class SheetEditAdapter(var mList: List<String>, val onRemove: (String) -> Unit) :
    RecyclerView.Adapter<SheetEditAdapter.ItemView>() {

    class ItemView(val binding: ItemSheetEditBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listItem: List<String>) {
        mList = listItem
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemSheetEditBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mList[position]) {
                binding.tvName.text = this
                binding.imvMinus.setOnClickListener {
                    onRemove(this)
                    notifyDataSetChanged()
                }
            }
        }
    }
}