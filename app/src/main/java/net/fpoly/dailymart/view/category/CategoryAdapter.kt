package net.fpoly.dailymart.view.category

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.param.CategoryParam
import net.fpoly.dailymart.databinding.ItemProductBinding
import net.fpoly.dailymart.extention.view_extention.gone
import net.fpoly.dailymart.view.category.AddCategoryDialog as toa


class CategoryAdapter(
    private val mContext: Context,
    private var mListCategory: List<CategoryParam>,
    private val onClick: (CategoryParam) -> Unit,
) : RecyclerView.Adapter<CategoryAdapter.ItemCategory>() {

    class ItemCategory(val binding: ItemProductBinding):RecyclerView.ViewHolder(binding.root)
    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryData(listCategory: List<CategoryParam>){
        mListCategory = listCategory
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCategory {
        return ItemCategory(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ItemCategory, position: Int) {
        with(holder){
            with(mListCategory[position]){
//                binding.tvId.text = this.id
                binding.tvName.text= this.name
                binding.tvPrice.gone()
                binding.tvId.gone()
                binding.imvImage.gone()
                binding.root.setOnClickListener{
                    onClick(this)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return mListCategory.size
    }


}

