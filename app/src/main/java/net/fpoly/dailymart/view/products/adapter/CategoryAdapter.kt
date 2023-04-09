package net.fpoly.dailymart.view.products.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.databinding.ItemNameOnlyBinding

class CategoryAdapter(
    private var mList: List<Category> = ArrayList(),
    val onChose: (Category) -> Unit,
) :
    RecyclerView.Adapter<CategoryAdapter.ItemView>() {


    class ItemView(val binding: ItemNameOnlyBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Category>){
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
                binding.name.text = this.name
                binding.root.setOnClickListener {
                    onChose(this)
                }
            }
        }
    }
}