package net.fpoly.dailymart.view.products.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.ItemNameOnlyBinding

class SupplierAdapter(
    private var mList: List<Supplier> = ArrayList(),
    val onChose: (Supplier) -> Unit,
) :
    RecyclerView.Adapter<SupplierAdapter.ItemView>() {

    class ItemView(val binding: ItemNameOnlyBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Supplier>){
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemNameOnlyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mList[position]) {
                binding.name.text = this.supplierName
                binding.root.setOnClickListener {
                    onChose(this)
                }
            }
        }
    }
}