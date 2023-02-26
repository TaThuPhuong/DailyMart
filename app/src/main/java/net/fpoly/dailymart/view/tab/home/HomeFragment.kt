package net.fpoly.dailymart.view.tab.home

import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.HomeFragmentBinding
import net.fpoly.dailymart.extention.view_extention.setMarginsStatusBar

class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {

    override fun setupData() {
        binding.layoutToolbar.setMarginsStatusBar(mContext)
    }

    override fun setupObserver() {


    }
}