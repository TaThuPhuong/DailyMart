package net.fpoly.dailymart.view.work_sheet

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.databinding.DialogChoseUserBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.view.work_sheet.adapter.UserAdapter

class ChoseUserDialog(
    private val mContext: Context,
    private val mList: List<UserRes>,
    val onChose: (UserRes) -> Unit,
) :
    BaseBottomDialog<DialogChoseUserBinding>(mContext, DialogChoseUserBinding::inflate) {

    override fun initData() {

        binding.imvClose.setOnClickListener { dismiss() }

        val mAdapter = UserAdapter(mList) {
            onChose(it)
            dismiss()
        }
        binding.rcvCategory.adapter = mAdapter

        binding.imvClear.setOnClickListener {
            binding.edSearch.setText("")
            binding.imvClear.gone()
            mAdapter.setData(mList)
        }
        binding.edSearch.getTextOnChange {
            val listFilter = mList.filter { user ->
                user.name.contains(it, true) || user.phoneNumber.contains(it, true)
            }
            if (it.isEmpty()) mAdapter.setData(mList)
            mAdapter.setData(listFilter)
        }
    }
}