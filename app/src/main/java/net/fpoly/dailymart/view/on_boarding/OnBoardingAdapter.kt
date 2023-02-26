package net.fpoly.dailymart.view.on_boarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.fpoly.dailymart.view.on_boarding.intro.FragmentIntro1
import net.fpoly.dailymart.view.on_boarding.intro.FragmentIntro2
import net.fpoly.dailymart.view.on_boarding.intro.FragmentIntro3

class OnBoardingAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentIntro1()
            1 -> FragmentIntro2()
            else -> FragmentIntro3()
        }
    }

}