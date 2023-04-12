package net.fpoly.dailymart.view.work_sheet

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val mListFrag: List<SheetDayFragment>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return mListFrag.size
    }

    override fun createFragment(position: Int): Fragment {
        return mListFrag[position]
    }
}