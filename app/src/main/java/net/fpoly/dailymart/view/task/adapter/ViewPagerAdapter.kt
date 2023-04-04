package net.fpoly.dailymart.view.task.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.fpoly.dailymart.view.task.TaskFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,private val listTitle :List<String>) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
       return listTitle.size
    }

    override fun createFragment(position: Int): Fragment {
       return TaskFragment.newInstance()
    }
}