package net.fpoly.dailymart.view.work_sheet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ItemSheetBinding

class SheetAdapter(var mList: List<String>) : RecyclerView.Adapter<SheetAdapter.ItemView>() {

    class ItemView(val binding: ItemSheetBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listItem: List<String>) {
        mList = listItem
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemSheetBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mList[position]) {
                binding.tvName.text = this
            }
        }
    }
}