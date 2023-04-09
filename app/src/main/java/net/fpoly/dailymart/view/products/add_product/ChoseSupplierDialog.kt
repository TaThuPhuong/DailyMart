package net.fpoly.dailymart.view.products.add_product

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.DialogChoseSupplierBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.view.products.adapter.SupplierAdapter

class ChoseSupplierDialog(
    private val mContext: Context,
    private val mList: List<Supplier>,
    val onChose: (Supplier) -> Unit,
) :
    BaseBottomDialog<DialogChoseSupplierBinding>(mContext, DialogChoseSupplierBinding::inflate) {

    override fun initData() {

        binding.imvClose.setOnClickListener { dismiss() }

        val mAdapter = SupplierAdapter(mList) {
            onChose(it)
            dismiss()
        }
        binding.rcvSupplier.adapter = mAdapter
        binding.imvClear.setOnClickListener {
            binding.edSearch.setText("")
            binding.imvClear.gone()
            mAdapter.setData(mList)
        }
        binding.edSearch.getTextOnChange {
            val listFilter = mList.filter { supplier ->
                supplier.supplierName.contains(it, true)
            }
            mAdapter.setData(listFilter)
        }
    }
}