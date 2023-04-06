package net.fpoly.dailymart.view.work_sheet

import androidx.fragment.app.activityViewModels
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.FragmentDaySheetBinding

class SheetDayFragment : BaseFragment<FragmentDaySheetBinding>(FragmentDaySheetBinding::inflate) {

    private val viewModel: WorkSheetViewModel by activityViewModels()

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun setupObserver() {

    }
}