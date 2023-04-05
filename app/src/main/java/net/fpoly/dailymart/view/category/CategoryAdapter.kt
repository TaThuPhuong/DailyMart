package net.fpoly.dailymart.view.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.databinding.ItemCategoryBinding

class CategoryAdapter(private val viewModel: CategoryViewModel) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    class CategoryViewHolder(val binding: ItemCategoryBinding) : ViewHolder(binding.root) {
        fun bind(viewModel: CategoryViewModel, item: Category) {
            binding.viewmodel = viewModel
            binding.category = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder.from(parent)

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("app:categoryItems")
fun setItemsCategory(list: RecyclerView, items: List<Category>?){
    items?.let {
        (list.adapter as CategoryAdapter).submitList(items)
    }
}

