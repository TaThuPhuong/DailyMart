package net.fpoly.dailymart.view.products.add_product

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.databinding.DialogChoseCategoryBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.view.products.adapter.CategoryAdapter

class ChoseCategoryDialog(
    private val mContext: Context,
    private val mListCategory: List<Category>,
    val onChose: (Category) -> Unit,
) :
    BaseBottomDialog<DialogChoseCategoryBinding>(mContext, DialogChoseCategoryBinding::inflate) {

    override fun initData() {

        binding.imvClose.setOnClickListener { dismiss() }

        val mAdapter = CategoryAdapter(mListCategory) {
            onChose(it)
            dismiss()
        }
        binding.rcvCategory.adapter = mAdapter

        binding.imvClear.setOnClickListener {
            binding.edSearch.setText("")
            binding.imvClear.gone()
            mAdapter.setData(mListCategory)
        }
        binding.edSearch.getTextOnChange {
            val listFilter = mListCategory.filter { category ->
                category.name.contains(it, true)
            }
            mAdapter.setData(listFilter)
        }
    }
}