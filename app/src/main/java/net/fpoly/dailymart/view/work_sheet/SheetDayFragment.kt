package net.fpoly.dailymart.view.work_sheet

import androidx.fragment.app.activityViewModels
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.FragmentDaySheetBinding

class SheetDayFragment(private val sheet: Sheet) :
    BaseFragment<FragmentDaySheetBinding>(FragmentDaySheetBinding::inflate) {

    private val viewModel: WorkSheetViewModel by activityViewModels()

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter1 = SheetAdapter(ArrayList())
        val adapter2 = SheetAdapter(ArrayList())
        val adapter3 = SheetAdapter(ArrayList())
        binding.rcvCa1.adapter = adapter1
        binding.rcvCa2.adapter = adapter2
        binding.rcvCa3.adapter = adapter3
        sheet.shift1?.let {
            adapter1.setData(it)
        }
        sheet.shift2?.let {
            adapter2.setData(it)
        }
        sheet.shift3?.let {
            adapter3.setData(it)
        }

    }

    override fun setupObserver() {

    }
}