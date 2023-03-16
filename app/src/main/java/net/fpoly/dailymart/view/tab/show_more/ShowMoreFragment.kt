package net.fpoly.dailymart.view.tab.show_more

import androidx.fragment.app.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.ShowMoreFragmentBinding

class ShowMoreFragment : BaseFragment<ShowMoreFragmentBinding>(ShowMoreFragmentBinding::inflate) {

    private val viewModel: ShowMoreViewModel by viewModels { AppViewModelFactory }
    override fun setOnClickListener() {

    }

    override fun setupData() {

    }

    override fun setupObserver() {

    }
}