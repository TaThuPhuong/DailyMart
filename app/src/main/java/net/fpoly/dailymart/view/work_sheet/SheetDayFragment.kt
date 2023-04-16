package net.fpoly.dailymart.view.work_sheet

import android.content.Intent
import androidx.fragment.app.activityViewModels
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.data.model.WorkSheet
import net.fpoly.dailymart.databinding.FragmentDaySheetBinding
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.work_sheet.adapter.SheetAdapter
import net.fpoly.dailymart.view.work_sheet.edit_work_sheet.EditWorkSheetActivity

class SheetDayFragment(private val sheet: WorkSheet) :
    BaseFragment<FragmentDaySheetBinding>(FragmentDaySheetBinding::inflate) {

    private val viewModel: WorkSheetViewModel by activityViewModels()

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val user = SharedPref.getUser(mContext)
        binding.imvAdd.setVisibility(user.role != ROLE.staff)

        val adapter1 = SheetAdapter(ArrayList())
        val adapter2 = SheetAdapter(ArrayList())
        val adapter3 = SheetAdapter(ArrayList())
        binding.rcvCa1.adapter = adapter1
        binding.rcvCa2.adapter = adapter2
        binding.rcvCa3.adapter = adapter3
        adapter1.setData(sheet.shift1)
        adapter2.setData(sheet.shift2)
        adapter3.setData(sheet.shift3)
        binding.imvAdd.setOnClickListener {
            val intent = Intent(mContext, EditWorkSheetActivity::class.java)
            intent.putExtra(Constant.WORK_SHEET, sheet)
            startActivity(intent)
        }
    }

    override fun setupObserver() {

    }
}